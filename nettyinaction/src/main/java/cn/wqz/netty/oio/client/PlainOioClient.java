package cn.wqz.netty.oio.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioSocketChannel;

import java.net.InetSocketAddress;

public class PlainOioClient {

    public static void main(String[] args) throws InterruptedException {

        String host = "127.0.0.1";
        int port = 8889;

        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new OioEventLoopGroup();

        try{
            bootstrap.channel(OioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .group(eventLoopGroup)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new PlainOioClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect().sync();
            future.channel().closeFuture().sync();

        }finally {
            eventLoopGroup.shutdownGracefully().sync();
        }

    }
}
