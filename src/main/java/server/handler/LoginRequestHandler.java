package server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.PacketCodeC;
import protocol.request.LoginRequestPacket;
import protocol.respond.LoginRespondPacket;
import session.Session;
import utils.LoginUtil;
import utils.SessionUtil;

import java.util.Objects;
import java.util.UUID;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) throws Exception {
        LoginRespondPacket loginRespondPacket = new LoginRespondPacket();
        if (valid(loginRequestPacket)) {
            // 校验成功
            loginRespondPacket.setReason("登录成功");
            loginRespondPacket.setSuccess(true);
            Session session = new Session();
            session.setUserName(loginRequestPacket.getUsername());
            session.setUserId(loginRequestPacket.getUsername());
            SessionUtil.bindSession(session,ctx.channel());
        } else {
            // 校验失败
            loginRespondPacket.setReason("用户名或密码错误");
            loginRespondPacket.setSuccess(false);
        }
        ctx.channel().writeAndFlush(loginRespondPacket);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Session session = SessionUtil.getSession(ctx.channel());
        System.out.println(session.getUserName()+" 退出");
        SessionUtil.unBindSession(ctx.channel());
    }


    private boolean valid(LoginRequestPacket loginRequestPacket) {
        System.out.println("登录信息："+loginRequestPacket);
        //群聊登录不检测用户名
//        if (Objects.equals(loginRequestPacket.getUsername(),"xupan")
//                && Objects.equals(loginRequestPacket.getPassword(),"1123")) {
//            return true;
//        } else {
//            return false;
//        }
        return true;
    }
}
