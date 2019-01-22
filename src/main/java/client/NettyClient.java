package client;

import client.handler.ClientHandler;
import client.handler.LoginResponseHandler;
import client.handler.MessageResponseHandler;
import codec.PacketDecoder;
import codec.PacketEncoder;
import codec.Spliter;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import protocol.PacketCodeC;
import protocol.request.MessageRequestPacket;
import utils.LoginUtil;

import java.util.Scanner;

public class NettyClient {
    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)// 1.指定线程模型
                //  2.指定 IO 类型为 NIO
        .channel(NioSocketChannel.class)
                //3.IO 处理逻辑
        .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                //ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));//长度域的起始偏移量、长度域长度
                ch.pipeline().addLast(new Spliter());
                ch.pipeline().addLast(new PacketDecoder());
                ch.pipeline().addLast(new LoginResponseHandler());
                ch.pipeline().addLast(new MessageResponseHandler());
                ch.pipeline().addLast(new PacketEncoder());
            }
        });

        // 4.建立连接
        bootstrap.connect("localhost", 6666).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
                ChannelFuture channelFuture = (ChannelFuture) future;
                startConsoleThread(channelFuture.channel());
            } else {
                System.err.println("连接失败!");
            }
        });

    }

    private static void startConsoleThread(Channel channel) {
        System.out.println("启动控制台通信程序-------->");
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (LoginUtil.hasLogin(channel)) {
                    System.out.println("输入消息发送至服务端: ");
                    Scanner sc = new Scanner(System.in);
                    String line = sc.nextLine();

                    MessageRequestPacket packet = new MessageRequestPacket();
                    packet.setMessage(line);
//                    ByteBuf byteBuf = PacketCodeC.INSTANCE.encode(channel.alloc().buffer(),packet);
                    channel.writeAndFlush(packet);
                }
            }
        }).start();
    }
}
