package cn.wqz.study.netty.coder.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 自定义head-content解码器
 */
public class StringIntegerHeaderDecoder extends ByteToMessageDecoder {
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() <4){
            return;
        }
        byteBuf.markReaderIndex(); // 标记当前索引位置
        int len = byteBuf.readInt();
        if(byteBuf.readableBytes() < len){
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes, 0, len);
        list.add(new String(bytes, "UTF-8"));
    }
}
