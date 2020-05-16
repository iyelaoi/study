package cn.wqz.study.netty.coder.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

/**
 * 双向编解码器测试
 */
public class Byte2IntegerCodecTester {
    @Test
    public void test(){
        ChannelInitializer channelInitializer = new ChannelInitializer<EmbeddedChannel>(){

            protected void initChannel(EmbeddedChannel embeddedChannel) throws Exception {
                embeddedChannel.pipeline().addLast(new Byte2IntegerCodec());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);

        ByteBuf byteBuf = Unpooled.buffer();
        for (int i = 0; i < 100; i++) {
            byteBuf.writeInt(i);
        }
        channel.writeInbound(byteBuf);

        for (int i = 0; i < 100; i++) {
            channel.write(i);
        }
        channel.flush();
        byteBuf = channel.readOutbound();
        while(null != byteBuf){
            System.out.println("bytebuf: " + byteBuf);
            Integer integer = byteBuf.readInt();
            System.out.println("channel read int:" + integer);
            byteBuf = channel.readOutbound();
        }
    }
}
