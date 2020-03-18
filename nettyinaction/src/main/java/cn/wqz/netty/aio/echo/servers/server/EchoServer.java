package cn.wqz.netty.aio.echo.servers.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {

    private final int port;

    public EchoServer(int port){
        this.port = port;
    }

    public static void main(String[] args) throws Exception{
/*        if(args.length != 1){
            System.out.println("Usage: " + EchoServer.class.getSimpleName()
                    + " <port>");
            return;
        }
        int port = Integer.parseInt(args[0]);*/
        new EchoServer(8889).start();
    }

    public void start() throws InterruptedException {
        final EchoServerHandler echoServerHandler = new EchoServerHandler();

        EventLoopGroup group = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        try{
            b.group(group)
                    .channel(NioServerSocketChannel.class) // set channel type
                    .localAddress(new InetSocketAddress(this.port))
                    .childHandler(new ChannelInitializer<SocketChannel>() { // add Child Handler to ChannelPipeline
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(echoServerHandler); // EchoServerHandler is shareable,
                            // so we can use this single instance of echoServerHandler
                        }
                    });
            ChannelFuture future = b.bind() //  bind to server
                    .sync(); // call sync() for block
            future.channel().closeFuture()
                    .sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }

}
