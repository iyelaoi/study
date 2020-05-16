package cn.wqz.study.netty.train;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class NettyNioClient {
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
        EventLoopGroup eventLoopGroup1 = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.remoteAddress(new InetSocketAddress("127.0.0.1", 9000));
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        ByteBuf byteBuf = (ByteBuf)msg;
                        System.out.println("client receive " + new String(byteBuf.array()));
                        super.channelRead(ctx, msg);
                    }
                });
                socketChannel.pipeline().addLast(new ChannelOutboundHandlerAdapter(){
                    @Override
                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                        System.out.println(msg);
                        super.write(ctx, msg, promise);
                    }
                });
            }
        });
        try {
            ChannelFuture channelFuture = bootstrap.connect().sync();
            System.out.println("connected");
            SocketChannel channel = (SocketChannel) channelFuture.channel();
            ByteBuf byteBuf = channel.alloc().buffer();
            Scanner scanner = new Scanner(System.in);
            while(scanner.hasNext()){
                String next = scanner.next();
                byteBuf.writeBytes(next.getBytes());
                channel.writeAndFlush(byteBuf);
                if("byebye".equals(next)){
                    System.out.println("byebye");
                    break;
                }
            }
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
            System.out.println("closed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
