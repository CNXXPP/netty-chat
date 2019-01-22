package server.handler;

import io.netty.buffer.ByteBuf;
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
import java.util.Objects;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf requestByteBuf = (ByteBuf) msg;

        // 解码
        Packet packet = PacketCodeC.INSTANCE.decode(requestByteBuf);
        // 判断是否是登录请求数据包
        Channel channel = ctx.channel();
        LoginRespondPacket loginRespondPacket = new LoginRespondPacket();
        if (packet instanceof LoginRequestPacket) {
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
            // 登录校验
            if (valid(loginRequestPacket)) {
                // 校验成功
                loginRespondPacket.setReason("登录成功");
                loginRespondPacket.setSuccess(true);
            } else {
                // 校验失败
                loginRespondPacket.setReason("用户名或密码错误");
                loginRespondPacket.setSuccess(false);
            }
            ByteBuf respondBuffer = PacketCodeC.INSTANCE.encode(loginRespondPacket);
            channel.writeAndFlush(respondBuffer);
        }else if (packet instanceof MessageRequestPacket) {
            MessageRequestPacket messageRequestPacket = (MessageRequestPacket) packet;
            System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());
            MessageRespondPacket messageResponsePacket = new MessageRespondPacket();
            messageResponsePacket.setMessage("服务端回复【" + messageRequestPacket.getMessage() + "】");
            ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(messageResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        System.out.println("登录信息："+loginRequestPacket);
        if (Objects.equals(loginRequestPacket.getUsername(),"xupan")
                && Objects.equals(loginRequestPacket.getPassword(),"1123")) {
            return true;
        } else {
            return false;
        }
    }
}
