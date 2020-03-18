package cn.wqz.netty.aio.echo.clients.client;

import io.netty.channel.*;

@ChannelHandler.Sharable
public class Echo2ClientHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("2" + msg);
        ctx.writeAndFlush(msg);
        System.out.println("2");
    }
}
