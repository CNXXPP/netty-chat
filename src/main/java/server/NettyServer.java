package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import protocol.PacketCodeC;
import protocol.request.MessageRequestPacket;
import server.handler.ServerHandler;
import utils.LoginUtil;

import java.util.Scanner;

public class NettyServer {
    private static final Integer serverPort=6666;

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ServerHandler());
                    }
                });
        serverBootstrap.bind(serverPort).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("服务器启动成功!");
            } else {
                System.err.println("服务器启动失败!");
            }

        });
    }
}
