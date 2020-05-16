package cn.wqz.study.netty.basic.echo;

import cn.wqz.study.netty.NettyDemoConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class NettyEchoClient {

    public static void runClient() throws InterruptedException {
        Bootstrap b = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        b.group(eventLoopGroup)
                .remoteAddress(NettyDemoConfig.SOCKET_SERVER_IP, NettyDemoConfig.SOCKET_SERVER_PORT)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .channel(NioSocketChannel.class)
                .handler(new NettyEchoClientHandler());
        ChannelFuture channelFuture = b.connect().sync();
        Channel channel = channelFuture.channel();
        System.out.println("client connected!");
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String msg = scanner.next();
            try {
                ByteBuf buf = Unpooled.copiedBuffer(msg.getBytes("UTF-8"));
                channel.writeAndFlush(buf);
                System.out.println("client send:" + msg);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        channel.closeFuture().sync();
        System.out.println("client closed!");
    }
    public static void main(String[] args) {
        try {
            runClient();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
