package cn.wqz.netty.aio.echo.clients.client;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


@ChannelHandler.Sharable
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel type: " + ctx.channel().getClass().getSimpleName());
        NioSocketChannel socketChannel = (NioSocketChannel) ctx.channel();
        ByteBuf byteBuf = Unpooled.copiedBuffer("test thread:", CharsetUtil.UTF_8).retain();
        ChannelFuture channelFuture = socketChannel.writeAndFlush(byteBuf);
        // 此处的listener只有下一个handler的 channelPromise 设置为success或failure 才会被触发
        // 也称做异常处理，
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(!channelFuture.isSuccess()){
                    // 如果不成功，打印异常堆栈，
                    // ChannelPromise.setFailure(Throwable cause)设置异常
                    channelFuture.cause().printStackTrace();
                    System.out.println("not success");
                }else{
                    System.out.println("success");
                }
            }
        });
        ScheduledFuture future = socketChannel.eventLoop().schedule(()->{
            System.out.println("EchoClientHandler active schedule");
        }, 10, TimeUnit.SECONDS);
        ScheduledFuture future1 = socketChannel.eventLoop().scheduleAtFixedRate(()->{
                    System.out.println("EchoClientHandler active scheduleAtFixedRate");
                    },
                10, 10, TimeUnit.SECONDS);
         // future1.cancel(true); // 注销操作：  true: 立即中断， false，本次完成后中断
        // 多线程发送导致信息覆盖问题
        /*Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // 当前线程并非支撑EventLoop的线程，所以写入任务会被加入到EventLoop队列等候调度
                socketChannel.writeAndFlush(byteBuf.duplicate());
                // 当前线程中所有非Channel生命周期事件，正常执行
                System.out.println(Thread.currentThread().getName() + " " + byteBuf);
            }
        };
        Executor executor = Executors.newCachedThreadPool();
        for(int i = 0; i <= 10; i++){
            executor.execute(runnable);
        }*/

    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object byteBuf) throws Exception {
        System.out.println("Client received: " + ((ByteBuf)byteBuf).toString(CharsetUtil.UTF_8));
        System.out.println(channelHandlerContext.pipeline().names());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
