package cn.wqz.netty.aio.echo.servers.server2;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class NioServerInHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server2 receive a connection from " + ctx.channel().remoteAddress());
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(ctx.channel().eventLoop())
                .channel(NioSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                        System.out.println("server client receive: " + byteBuf.toString());
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8889));
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(channelFuture.isSuccess()){
                    System.out.println("server client connect success");
                }else{
                    System.out.println("server client connect failed");
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server2 receive a message: " + ((ByteBuf)msg).toString());
        ctx.writeAndFlush(msg);
    }
}
