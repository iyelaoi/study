package cn.wqz.netty.aio.echo.servers.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;


@ChannelHandler.Sharable // 标识一个ChannelHandler 可以被多个Channel共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf)msg;
        if(in.hasArray()){ // 堆缓冲区， 支撑数组， 非池化
            System.out.println("buf has array");
            byte[] array = in.array(); // 支撑数组可直接获取数组
            System.out.println(array.length);
            int offset = in.arrayOffset() + in.readerIndex();
            int length = in.readableBytes();
            System.out.println("server received: " + new String(array, offset, length));
        }else { // 直接缓冲区，不能直接获取数组
            System.out.println("buf has not array");
            int length = in.readableBytes();
            byte[] array = new byte[length];
            in.getBytes(in.readerIndex(), array);
            System.out.println("server received: " + new String(array, 0, length));
        }
        System.out.println(in);
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
        System.out.println(in);
        ctx.writeAndFlush(in); // send the msg to client and no flush

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // flush pending message, saving in ChannelOutboundBuffer, to remote node
        // and close the Channel
        System.out.println("read completed");
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
