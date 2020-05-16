package cn.wqz.study.netty.coder.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 不需要进行缓冲区字节数判断
 * ReplayingDecoder 内部对ByteBuf进行了装饰，已经完成了判断步骤
 *
 */
public class Byte2IntegerReplayDecoder extends ReplayingDecoder {
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int i = byteBuf.readInt();
        System.out.println("解码出整数：" + i);
        list.add(i);
    }
}
