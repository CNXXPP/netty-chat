package server;

import codec.PacketCodecHandler;
import codec.PacketDecoder;
import codec.PacketEncoder;
import codec.Spliter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import server.handler.AuthHandler;
import server.handler.LoginRequestHandler;
import server.handler.MessageRequestHandler;

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
                        //ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));//长度域的起始偏移量、长度域长度
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new PacketCodecHandler());
                        //ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginRequestHandler());
                        ch.pipeline().addLast(new AuthHandler());
                        ch.pipeline().addLast(new MessageRequestHandler());
                        //ch.pipeline().addLast(new PacketEncoder());
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
