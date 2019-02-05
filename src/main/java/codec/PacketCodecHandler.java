package codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import protocol.Packet;
import protocol.PacketCodeC;
public class PacketCodecHandler extends MessageToMessageCodec<ByteBuf, Packet>{

//	 public static final PacketCodecHandler INSTANCE = new PacketCodecHandler();
//
//	 public PacketCodecHandler() {
//
//	 }
	    
	@Override
	protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
		out.add(PacketCodeC.INSTANCE.encode(ctx.channel().alloc().ioBuffer(),msg));
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		out.add(PacketCodeC.INSTANCE.decode(msg));		
	}

}
