package server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.PacketCodeC;
import protocol.request.LoginRequestPacket;
import protocol.respond.LoginRespondPacket;
import utils.LoginUtil;

import java.util.Objects;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginRequestPacket loginRequestPacket) throws Exception {
        LoginRespondPacket loginRespondPacket = new LoginRespondPacket();
        if (valid(loginRequestPacket)) {
            // 校验成功
            loginRespondPacket.setReason("登录成功");
            loginRespondPacket.setSuccess(true);
            LoginUtil.markAsLogin(channelHandlerContext.channel());
        } else {
            // 校验失败
            loginRespondPacket.setReason("用户名或密码错误");
            loginRespondPacket.setSuccess(false);
        }
        channelHandlerContext.channel().writeAndFlush(loginRespondPacket);
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
