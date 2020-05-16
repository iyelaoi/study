package cn.wqz.study.netty.coder.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * MessageToMessageDecoder
 * Integer 向 String解码
 */
public class Integer2StringDecoder extends MessageToMessageDecoder<Integer> {
    protected void decode(ChannelHandlerContext channelHandlerContext, Integer integer, List<Object> list) throws Exception {
        list.add(String.valueOf(integer));
    }
}
