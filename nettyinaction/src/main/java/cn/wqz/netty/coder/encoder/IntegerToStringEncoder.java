package cn.wqz.netty.coder.encoder;

import cn.wqz.netty.coder.decoder.messagetomessage.IntegerToStringDecoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 *
 */
public class IntegerToStringEncoder extends MessageToMessageEncoder<Integer> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Integer integer, List<Object> list) throws Exception {
        System.out.println("encoder: " + integer);
        list.add(String.valueOf(integer)); // 将Integer转为String保存
    }

    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(new IntegerToStringEncoder());
        channel.writeOutbound(100);
        System.out.println("over");
        String s = channel.readOutbound();
        System.out.println(s);

    }


}
