package cn.wqz.study.netty.basic.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyEchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        int len = byteBuf.readableBytes();
        byte[] bytes = new byte[len];
        byteBuf.getBytes(byteBuf.readerIndex(), bytes);
        String m = new String(bytes, "UTF-8");
        System.out.println("server receive " + ctx.channel().remoteAddress() + " : " + m);
        ctx.channel().writeAndFlush(msg);
    }
}
