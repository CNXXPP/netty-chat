package client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import protocol.Packet;
import protocol.PacketCodeC;
import protocol.request.LoginRequestPacket;
import protocol.request.MessageRequestPacket;
import protocol.respond.LoginRespondPacket;
import protocol.respond.MessageRespondPacket;
import utils.LoginUtil;

import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

public class ClientHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + ": 客户端开始登录");

        // 创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("xupan");
        loginRequestPacket.setPassword("1123");

        // 编码
        ByteBuf buffer = PacketCodeC.INSTANCE.encode(loginRequestPacket);

        // 写数据
        ctx.channel().writeAndFlush(buffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        Packet decode = PacketCodeC.INSTANCE.decode(byteBuf);
        if (decode instanceof LoginRespondPacket) {
            LoginRespondPacket loginRespondPacket = (LoginRespondPacket) decode;
            if (loginRespondPacket.isSuccess()) {
                System.out.println("我已登录成功");
                //添加登录成功标志位
                LoginUtil.markAsLogin(ctx.channel());
            }else {
                System.out.println("怎么就失败了----->"+loginRespondPacket.getReason());
            }
        }else if (decode instanceof MessageRespondPacket) {
            MessageRespondPacket messageResponsePacket = (MessageRespondPacket) decode;
            System.out.println(new Date() + ": 收到服务端的消息: " + messageResponsePacket.getMessage());
        }

    }
}
