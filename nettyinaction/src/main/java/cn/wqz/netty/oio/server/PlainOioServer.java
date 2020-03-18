package cn.wqz.netty.oio.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;

public class PlainOioServer {
    public static void main(String[] args) throws InterruptedException {
        int port = 8889;
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup eventLoopGroup = new OioEventLoopGroup();
        try{
            PlainOioServerHandler serverHandler = new PlainOioServerHandler();
            serverBootstrap.group(eventLoopGroup)
                    .localAddress(new InetSocketAddress(port))
                    .channel(OioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(serverHandler);
                        }
                    });
            ChannelFuture future = serverBootstrap.bind().sync();
            future.channel().closeFuture().sync();

        }finally {
            eventLoopGroup.shutdownGracefully().sync();
        }
    }
}
