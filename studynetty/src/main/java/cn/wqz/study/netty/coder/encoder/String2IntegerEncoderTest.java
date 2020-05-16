package cn.wqz.study.netty.coder.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

public class String2IntegerEncoderTest {

    @Test
    public void test(){
        ChannelInitializer channelInitializer = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel channel) throws Exception {
                channel.pipeline().addLast(new Integer2ByteEncoder());
                channel.pipeline().addLast(new String2IntegerEncoder());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);
        for (int i = 0; i < 100; i++) {
            String s = "i am " + i;
            channel.write(s);
        }
        channel.flush();
        ByteBuf byteBuf = (ByteBuf)channel.readOutbound();
        while(null != byteBuf){
            System.out.println("o = " + byteBuf.readInt());
            byteBuf = (ByteBuf)channel.readOutbound();
        }
    }
}
