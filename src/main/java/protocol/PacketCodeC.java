package protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import protocol.command.CommandConstants;
import protocol.request.LoginRequestPacket;
import protocol.respond.LoginRespondPacket;
import serialize.Serializer;
import serialize.SerializerAlgorithmConstants;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装成二进制
 *
 */

public class PacketCodeC {

    public static final int MAGIC_NUMBER = 0x12345678;
    public static final PacketCodeC INSTANCE = new PacketCodeC();

    private final Map<Byte, Class<? extends Packet>> packetTypeMap; //不同指令对应请求包类型
    private final Map<Byte, Serializer> serializerMap; //不同序列化算法对应序列化对象

    public PacketCodeC() {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(CommandConstants.LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(CommandConstants.LOGIN_RESPONSE, LoginRespondPacket.class);
        serializerMap = new HashMap<>();
        serializerMap.put(SerializerAlgorithmConstants.JSON,Serializer.DEFAULT);
    }

    public ByteBuf encode(Packet packet) {
        // 1. 创建 ByteBuf 对象,Netty 的 ByteBuf 分配器来创建，ioBuffer() 方法会返回适配 io 读写相关的内存，它会尽可能创建一个直接内存，
        // 直接内存可以理解为不受 jvm 堆管理的内存空间，写到 IO 缓冲区的效果更高。
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        // 2. 序列化 Java 对象
        // 接下来，我们将 Java 对象序列化成二进制数据包
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 3. 实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER); //4字节32位
        byteBuf.writeByte(packet.getVersion());//版本号 1个字节 8位
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm()); //序列化算法 1字节 8位
        byteBuf.writeByte(packet.getCommand()); //指令 1字节 8位
        byteBuf.writeInt(bytes.length); //数据部分的长度 4字节 32位
        byteBuf.writeBytes(bytes);//数据部分 n字节

        return byteBuf;
    }

    public Packet decode(ByteBuf byteBuf) {
        // 跳过 magic number
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 序列化算法标识
        byte serializeAlgorithm = byteBuf.readByte();

        // 指令
        byte command = byteBuf.readByte();

        // 数据包长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }

        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {
        return packetTypeMap.get(command);
    }
}
