package cn.wqz.study.netty.coder.encoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class String2IntegerEncoder extends MessageToMessageEncoder<String> {
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, List<Object> list) throws Exception {
        char[] array = s.toCharArray();
        for (char a : array){
            if (a >= 48 && a <= 57){
                list.add(new Integer(a));
            }
        }
    }
}
