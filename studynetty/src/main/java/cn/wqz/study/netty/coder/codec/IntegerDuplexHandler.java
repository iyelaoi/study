package cn.wqz.study.netty.coder.codec;

import cn.wqz.study.netty.coder.decoder.Byte2IntegerDecoder;
import cn.wqz.study.netty.coder.encoder.Integer2ByteEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

public class IntegerDuplexHandler extends CombinedChannelDuplexHandler<Byte2IntegerDecoder, Integer2ByteEncoder> {
    public IntegerDuplexHandler(){
        super(new Byte2IntegerDecoder(), new Integer2ByteEncoder());
    }

    @Test
    public void test(){
        ChannelInitializer channelInitializer = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new IntegerDuplexHandler());
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
