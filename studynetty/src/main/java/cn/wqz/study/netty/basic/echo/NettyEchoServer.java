package cn.wqz.study.netty.basic.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyEchoServer {
    public static void main(String[] args) throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup g1 = new NioEventLoopGroup(1);
        EventLoopGroup g2 = new NioEventLoopGroup();
        serverBootstrap.group(g1, g2);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.localAddress(9000);
        serverBootstrap.option(ChannelOption.SO_KEEPALIVE ,true);
        serverBootstrap.option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new NettyEchoServerHandler());
            }
        });
        ChannelFuture channelFuture = serverBootstrap.bind().sync();
        System.out.println("server listen port:" + channelFuture.channel().localAddress());
        ChannelFuture closeFuture = channelFuture.channel().closeFuture();
        closeFuture.sync();
        System.out.println("server closed");
        g2.shutdownGracefully();
        g1.shutdownGracefully();

    }
}
