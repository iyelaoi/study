package cn.wqz.study.netty.coder.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

/**
 * 整形编码器测试
 */
public class Integer2ByteEncoderTest {

    @Test
    public void test(){
        ChannelInitializer channelInitializer = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel channel) throws Exception {
                channel.pipeline().addLast(new Integer2ByteEncoder());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);
        for (int i = 0; i < 100; i++){
            channel.write(i); // 向通道中写入数据
        }
        channel.flush(); // 刷新
        ByteBuf byteBuf = (ByteBuf) channel.readOutbound(); // 从通道中读取ByteBuf
        while(null != byteBuf) {
            System.out.println("o = " + byteBuf.readInt());  // 获取ByteBuf中的内容
            byteBuf = (ByteBuf) channel.readOutbound(); // 再次获取Buf
        }
    }
}
