package cn.wqz.netty.coder.decoder.messagetomessage;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 将Integer转为String解码器
 */
public class IntegerToStringDecoder extends MessageToMessageDecoder<Integer> {
    /**
     * 仅处理Integer类型的数据，对于Buf类型的写入不响应
     * @param channelHandlerContext
     * @param integer
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, Integer integer, List<Object> list) throws Exception {
        // 将Integer转换为String
        System.out.println("decode:" + integer);
        list.add(String.valueOf(integer));
    }

    public static void main(String[] args) {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new IntegerToStringDecoder());
        embeddedChannel.writeInbound(100);
        // embeddedChannel.writeInbound(Unpooled.copiedBuffer(new byte[]{0,0,0,1}));
        embeddedChannel.finish();

        String s = embeddedChannel.readInbound();
        System.out.println("readInbound() = " + s);
    }
}
