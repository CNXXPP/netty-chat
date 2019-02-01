package server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.request.MessageRequestPacket;
import protocol.respond.MessageRespondPacket;
import session.Session;
import utils.SessionUtil;

public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket>{
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageRequestPacket messageRequestPacket) throws Exception {
        System.out.println("收到客户端消息：------->"+messageRequestPacket.getMessage());
        System.out.println("发送给：---------->"+messageRequestPacket.getToUserId());
        Channel toUserChannel = SessionUtil.getChannel(messageRequestPacket.getToUserId());
        if (toUserChannel == null) {
            System.out.println("信息转发失败");
            MessageRespondPacket messageRespondPacket = new MessageRespondPacket();
            messageRespondPacket.setMessage(messageRequestPacket.getToUserId()+" 不在线，信息发送失败");
            messageRespondPacket.setFromUserName("服务器");
            channelHandlerContext.channel().writeAndFlush(messageRespondPacket);
        }else{
            System.out.println("信息转发成功");
            MessageRespondPacket messageRespondPacket = new MessageRespondPacket();
            messageRespondPacket.setMessage(messageRequestPacket.getMessage());
            Session session = SessionUtil.getSession(channelHandlerContext.channel());
            messageRespondPacket.setFromUserName(session.getUserName());
            toUserChannel.writeAndFlush(messageRespondPacket);
        }

    }


}
