package cn.wqz.netty.aio.echo.clients.client;

import io.netty.channel.*;

/**
 * 出站控制器，
 * 按照pipeline的逆序，
 * 每个handler中使用 ChannelHandlerContext的一些方法，将事件传播给下一个Handler
 */
@ChannelHandler.Sharable
public class Echo3ClientHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("3" + msg);
        ChannelFuture channelFuture = ctx.writeAndFlush(msg);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(!channelFuture.isSuccess()){
                    System.out.println("not success");
                }else{
                    System.out.println("success");
                }
            }
        });
        promise.setSuccess();
    }
}
