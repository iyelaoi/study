package cn.wqz.study.netty.serializer;

import cn.wqz.study.netty.NettyDemoConfig;
import cn.wqz.study.netty.basic.echo.NettyEchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.UnsupportedEncodingException;

/**
 * 客户端发送大量数据
 * 模拟半包、粘包问题
 */
public class NettyDumpSendClient {

    public static void runClient() throws InterruptedException, UnsupportedEncodingException {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup);
        bootstrap.remoteAddress(NettyDemoConfig.SOCKET_SERVER_IP, NettyDemoConfig.SOCKET_SERVER_PORT);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new NettyEchoClientHandler());
            }
        });
        ChannelFuture f = bootstrap.connect();
        f.sync();
        System.out.println("DumpClient connected");
        Channel channel = f.channel();
        String s = "曾经沧海难为水，除去巫山不是云";
        byte[] bytes = s.getBytes("UTF-8");
        for (int i = 0; i < 1000; i++) {
            ByteBuf byteBuf = channel.alloc().buffer();
            byteBuf.writeBytes(bytes);
            channel.writeAndFlush(byteBuf);
        }
        channel.closeFuture().sync();
        eventLoopGroup.shutdownGracefully();
    }

    public static void main(String[] args) {
        try {
            runClient();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
