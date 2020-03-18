package cn.wqz.netty.coder.decoder.tointeger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.junit.Assert;

import java.util.List;
import static org.junit.Assert.*;


/**
 * demo: 解码器
 * 从字节流缓冲区中解码出int型数据，每一个int值作为单独的数据处理，放入list
 */
public class ToIntegerDecoder extends ByteToMessageDecoder {

    /**
     * 该方法将会被重复调用，直到确定没有新的元素加到该List，或者buyeBuf中没有更多的可读字节
     *
     * 一旦消息被解码，消息就会被自动释放： ReferenceCountUtil.release(message)
     * 如果你需要保留引用以便稍后使用，那么，你可以调用
     * ReferenceCountUtil.retain(message)增加引用计数，从而方式消息被释放
     * @param channelHandlerContext
     * @param byteBuf
     * @param list 如果list不为空，那么他的内容将会被传递给ChannelPipeline中的下一个ChannelHandler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("decode: " + byteBuf);
        while(byteBuf.readableBytes() > 3){
            list.add(byteBuf.readInt());
        }
    }

}
