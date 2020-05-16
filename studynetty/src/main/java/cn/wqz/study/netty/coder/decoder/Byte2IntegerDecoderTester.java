package cn.wqz.study.netty.coder.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

public class Byte2IntegerDecoderTester {
    @Test
    public void testByteToIntegerDecoder() throws InterruptedException {
        ChannelInitializer channelInitializer = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                //channel.pipeline().addLast(new Byte2IntegerDecoder());
                //channel.pipeline().addLast(new Byte2IntegerReplayDecoder());
                channel.pipeline().addLast(new IntegerAddDecoder());
                channel.pipeline().addLast(new IntegerProcessHandler());

            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);
        for (int i = 0; i < 100; i++){
            ByteBuf byteBuf = Unpooled.buffer();
            byteBuf.writeInt(i);
            channel.writeInbound(byteBuf);
        }
        System.out.println("output completed");

    }
}
