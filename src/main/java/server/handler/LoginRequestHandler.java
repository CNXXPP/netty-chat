package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.request.LoginRequestPacket;
import protocol.respond.LoginRespondPacket;
import session.Session;
import utils.SessionUtil;


public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {



    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel绑定到线程，channelRegistered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel解绑，channelUnregistered");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel准备就绪，channelActive");
        super.channelActive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel读完某次数据，channelReadComplete");
        super.channelReadComplete(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) throws Exception {
        System.out.println("channel 有数据可读：channelRead()");
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
        System.out.println("channel关闭：channelInactive()");
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
