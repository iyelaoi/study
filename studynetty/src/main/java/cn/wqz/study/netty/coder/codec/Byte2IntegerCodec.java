package cn.wqz.study.netty.coder.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

/**
 * 双向编解码器
 * 编解码器逻辑在一个类中实现，耦合度较高
 */
public class Byte2IntegerCodec extends ByteToMessageCodec<Integer> {
    protected void encode(ChannelHandlerContext channelHandlerContext, Integer integer, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(integer);
        System.out.println("Write Integer: " + integer);
    }

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf.readableBytes() >= 4){
            Integer integer = byteBuf.readInt();
            System.out.println("Decoder int: " + integer);
            list.add(integer);
        }
    }
}
