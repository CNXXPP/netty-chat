package client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.respond.MessageRespondPacket;

public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageRespondPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageRespondPacket messageRespondPacket) throws Exception {
        System.out.println("收到服务端消息------->");
        System.out.println(messageRespondPacket.getMessage());
    }
}
