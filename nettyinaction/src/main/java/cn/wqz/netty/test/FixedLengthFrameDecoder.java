package cn.wqz.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 定义 handler（解码器）
 * 收到缓存数据达到一定长度时进行数据处理
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {

    private final int frameLength;

    public FixedLengthFrameDecoder(int frameLength){
        if(frameLength <= 0){
            throw new IllegalArgumentException("frameLength must be a positive integer: " + frameLength);
        }
        this.frameLength = frameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("length: " + byteBuf.readableBytes());
        while(byteBuf.readableBytes() >= this.frameLength){
            ByteBuf buf = byteBuf.readBytes(this.frameLength);
            list.add(buf);
        }
    }
}
