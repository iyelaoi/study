package cn.wqz.server2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

public class EchoServer {

    public static void main(String[] args) throws Exception {

        // bossGroup, 监听连接线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        // workGroup, 工作线程组
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            // 服务启动器
            ServerBootstrap sb = new ServerBootstrap();

            // 配置操作系统连接队列的最大容量，
            // 服务器端TCP内核模块维护有2个队列，我们称之为A，B吧
            // 客户端向服务端connect的时候，发送带有SYN标志的包（第一次握手）
            // 服务端收到客户端发来的SYN时，向客户端发送SYN ACK 确认(第二次握手)
            // 此时TCP内核模块把客户端连接加入到A队列中，然后服务器收到客户端发来的ACK时（第三次握手）
            // TCP没和模块把客户端连接从A队列移到B队列，连接完成，应用程序的accept会返回
            // 也就是说accept从B队列中取出完成三次握手的连接
            //
            // A队列和B队列的长度之和是backlog,当A，B队列的长之和大于backlog时，新连接将会被TCP内核拒绝
            //
            // 所以，如果backlog过小，可能会出现accept速度跟不上，A.B 队列满了，导致新客户端无法连接，
            //
            //
            // 要注意的是，backlog对程序支持的连接数并无影响，backlog影响的只是还没有被accept 取出的连接
            sb.option(ChannelOption.SO_BACKLOG, 1024);

            sb.group(group, bossGroup) // 绑定线程池
                    .channel(NioServerSocketChannel.class) // 指定使用的channel类型
                    .localAddress(9999)// 绑定监听端口
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            System.out.println("报告");
                            System.out.println("信息：有一客户端链接到本服务端");
                            System.out.println("IP:" + ch.localAddress().getHostName());
                            System.out.println("Port:" + ch.localAddress().getPort());
                            System.out.println("报告完毕");

                            ch.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));
                            ch.pipeline().addLast(new EchoServerHandler()); // 客户端触发操作
                            ch.pipeline().addLast(new ByteArrayEncoder());
                        }
                    });
            ChannelFuture cf = sb.bind().sync(); // 服务器异步创建绑定
            System.out.println(EchoServer.class + " 启动正在监听： " + cf.channel().localAddress());

            cf.channel().closeFuture().sync(); // 关闭服务器通道
            System.out.println("over");
        } finally {
            group.shutdownGracefully().sync(); // 释放线程池资源
            bossGroup.shutdownGracefully().sync();
        }
    }
}
