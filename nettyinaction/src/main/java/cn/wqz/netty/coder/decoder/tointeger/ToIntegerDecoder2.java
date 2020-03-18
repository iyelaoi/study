package cn.wqz.netty.coder.decoder.tointeger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * RepalyingDecoder继承ByteToMessageDecoder并进行了扩展
 * 是我们不需主动调用readableBytes()方法
 *
 */
public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {
    /**
     * 每次调用此方法只能进行一个数据处理
     * 若buf中还有数据，会自动重新调用此方法（效率上会有一定的影响）
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("decode: " + byteBuf);
        // 如果没有足够的字节可用，则会抛出一个异常
        list.add(byteBuf.readInt());
    }
}
