package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.request.MessageRequestPacket;
import protocol.respond.MessageRespondPacket;

public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket>{
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageRequestPacket messageRequestPacket) throws Exception {
        System.out.println("收到客户端消息：------->");
        System.out.println(messageRequestPacket.getMessage());
        MessageRespondPacket messageRespondPacket = new MessageRespondPacket();
        messageRespondPacket.setMessage("服务器回复：["+messageRequestPacket.getMessage()+"]");
        channelHandlerContext.channel().writeAndFlush(messageRespondPacket);
    }
}
