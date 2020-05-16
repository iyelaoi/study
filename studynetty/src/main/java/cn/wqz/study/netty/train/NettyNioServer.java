package cn.wqz.study.netty.train;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyNioServer {
    private static Logger logger = LoggerFactory.getLogger(NettyNioServer.class);

    public static void main(String[] args) throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
        EventLoopGroup eventLoopGroup1 = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup,eventLoopGroup1);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.localAddress(9000);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        ByteBuf byteBuf = (ByteBuf)msg;
                        System.out.println("server receive " + ctx.channel().remoteAddress() + " : " + byteBuf);
                        super.channelRead(ctx, msg);
                    }

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        logger.info("active " + ctx.channel().remoteAddress());
                        super.channelActive(ctx);
                    }

                    @Override
                    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                        logger.info("registered " + ctx.channel().remoteAddress());
                        super.channelRegistered(ctx);
                    }

                    @Override
                    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                        logger.info("readComplete " + ctx.channel().remoteAddress());
                        super.channelReadComplete(ctx);
                    }

                    @Override
                    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                        logger.info("unregistered " + ctx.channel().remoteAddress());
                        super.channelUnregistered(ctx);
                    }

                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        logger.info("inactive " + ctx.channel().remoteAddress());
                        super.channelInactive(ctx);
                    }

                    @Override
                    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
                        logger.info("WritabilityChanged " + ctx.channel().remoteAddress());
                        super.channelWritabilityChanged(ctx);
                    }
                });
            }
        });
        ChannelFuture channelFuture = bootstrap.bind().sync();
        System.out.println("server bind completed!!");
        ChannelFuture closeFuture = channelFuture.channel().closeFuture();
        closeFuture.sync();
        System.out.println("server closed!!");
        eventLoopGroup1.shutdownGracefully();
        eventLoopGroup.shutdownGracefully();
    }
}
