package cn.wqz.study.netty.coder.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * head-content模式的分包解码测试
 * 有乱码问题没有解决
 * ReplayDecoder局限性
 * 1. ReplayDecoder是有一些局限性的，不是所有的ByteBuf都被ReplayDecoder支持，可能会抛ReplayError异常
 * 2. 在ByteBuf长度不够时，会通过ReplayError并捕获，然后进行断点指针还原等工作，当网络不好，分包较多，就会导致效率问题
 */
public class StringReplayDecoderTester {

    @Test
    public void test() throws UnsupportedEncodingException {
        Random random = new Random(47);
        ChannelInitializer channelInitializer = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                //channel.pipeline().addLast(new StringIntegerHeaderDecoder());
                channel.pipeline().addLast(new StringReplayDecoder()); // 不建议使用
                channel.pipeline().addLast(new StringProcessHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);
        String msg = "曾经沧海难为水，除去巫山不是云";
        byte[] bytes = msg.getBytes("UTF-8");
        for(int i = 0; i < 100; i++){
            int rand = random.nextInt(3) + 1;
            ByteBuf byteBuf = Unpooled.buffer();
            byteBuf.writeInt(bytes.length * rand);
            for (int k = 0; k < rand; k++){
                byteBuf.writeBytes(bytes);
            }
            System.out.println(byteBuf);
            channel.writeInbound(byteBuf);
        }
    }
}
