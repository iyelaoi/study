package cn.wqz.study.netty.coder.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class Byte2IntegerDecoder extends ByteToMessageDecoder {
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf.readableBytes() >= 4){
            int i = byteBuf.readInt();
            System.out.println("解码出一个整数：" + i);
            list.add(i);
        }
    }
}
