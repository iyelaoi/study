package cn.wqz.netty.aio.echo.clients.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(this.host, this.port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new EchoClientHandler())
                                    .addLast(new Echo2ClientHandler())
                                    .addLast(new Echo3ClientHandler());
                        }
                    });
            ChannelFuture future = b.connect().sync();
            System.out.println("connected");
            future.channel().closeFuture().sync();
            System.out.println("closed");
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
/*        if(args.length != 2){
            System.out.println("Usage: " + EchoClient.class.getSimpleName()
                    + " <host> <port>");
            return;
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);*/
        new EchoClient("127.0.0.1", 8888).start();

    }
}

