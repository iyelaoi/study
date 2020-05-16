package cn.wqz.study.netty.coder.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class IntegerProcessHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Integer integer = (Integer)msg;
        System.out.println("receive a Integer: " + integer);
        super.channelRead(ctx, msg);
    }
}
