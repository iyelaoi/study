package cn.wqz.study.netty.coder.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 整形编码器
 */
public class Integer2ByteEncoder extends MessageToByteEncoder<Integer> {
    protected void encode(ChannelHandlerContext channelHandlerContext, Integer integer, ByteBuf byteBuf) throws Exception {
        // 将整形写入buf
        byteBuf.writeInt(integer);
        System.out.println("Encoder Integer = " + integer);
    }
}
