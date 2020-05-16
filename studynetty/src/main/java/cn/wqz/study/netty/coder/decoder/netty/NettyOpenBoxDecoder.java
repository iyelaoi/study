package cn.wqz.study.netty.coder.decoder.netty;

import cn.wqz.study.netty.coder.decoder.StringProcessHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Random;

/**
 * netty自带的解码器
 */
public class NettyOpenBoxDecoder {

    String splitString = "\r\n";
    String delimited = ";";
    String content = "七八个星天外，两三点雨山前";

    /**
     * 测试行解码器
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testLineBasedFrameDecoder() throws UnsupportedEncodingException {
        ChannelInitializer channelInitializer = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new LineBasedFrameDecoder(1024)); // 按行分包
                channel.pipeline().addLast(new StringDecoder());
                channel.pipeline().addLast(new StringProcessHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);
        Random random = new Random(47);
        byte[] bytes = content.getBytes("UTF-8");
        byte[] bytes1 = splitString.getBytes("UTF-8");
        for (int i = 0; i < 100; i++){
            ByteBuf byteBuf = Unpooled.buffer();
            int n = random.nextInt(3)+1;
            for(int k = 0; k < n; k++){
                byteBuf.writeBytes(bytes);
            }
            byteBuf.writeBytes(bytes1);
            channel.writeInbound(byteBuf);
        }
    }

    /**
     * 测试分隔符解码器
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testDelimiterBasedFrameDecoder() throws UnsupportedEncodingException {
        ChannelInitializer channelInitializer = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel channel) throws Exception {
                final ByteBuf delimiter = Unpooled.copiedBuffer(delimited.getBytes("UTF-8"));
                channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, true, delimiter));
                channel.pipeline().addLast(new StringDecoder());
                channel.pipeline().addLast(new StringProcessHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);
        Random random = new Random(47);
        byte[] bytes = content.getBytes("UTF-8");
        byte[] bytes1 = delimited.getBytes("UTF-8");
        for (int i = 0; i < 100; i++){
            ByteBuf byteBuf = Unpooled.buffer();
            int n = random.nextInt(3)+1;
            for(int k = 0; k < n; k++){
                byteBuf.writeBytes(bytes);
            }
            byteBuf.writeBytes(bytes1);
            channel.writeInbound(byteBuf);
        }
    }

    /**
     * 基本的 长度 + 内容型（head-content）
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testLengthFieldBasedFrameDecoder() throws UnsupportedEncodingException {
        final LengthFieldBasedFrameDecoder lengthFieldBasedFrameDecoder =
                new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4);
        ChannelInitializer channelInitializer = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel channel) throws Exception {

                channel.pipeline().addLast(lengthFieldBasedFrameDecoder);
                channel.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
                channel.pipeline().addLast(new StringProcessHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);
        Random random = new Random(47);
        byte[] bytes = content.getBytes("UTF-8");
        for (int i = 0; i < 100; i++){
            ByteBuf byteBuf = Unpooled.buffer();
            int n = random.nextInt(3)+1;
            byte[] bytes1 = ("第" + i +"次发送").getBytes("UTF-8");
            byteBuf.writeInt(n * bytes.length + bytes1.length);// 长度
            byteBuf.writeBytes(bytes1);
            for(int k = 0; k < n; k++){
                byteBuf.writeBytes(bytes);
            }
            channel.writeInbound(byteBuf);
        }
    }

    /**
     * 复杂的（head-content）
     * 版本号（2byte） + 长度（4byte） + 魔数（4byte，安全认证使用）
     * 解码器自动进行了信息的过滤，但这些信息没有发挥任何作用
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testLengthFieldBasedFrameDecoder2() throws UnsupportedEncodingException {
        final char VERSION = 1000;
        final int MAGICAL = 461299;
        // 核心解码器
        final LengthFieldBasedFrameDecoder lengthFieldBasedFrameDecoder =
                new LengthFieldBasedFrameDecoder(1024, 2, 4, 4, 10);
        ChannelInitializer channelInitializer = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel channel) throws Exception {

                channel.pipeline().addLast(lengthFieldBasedFrameDecoder);
                channel.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
                channel.pipeline().addLast(new StringProcessHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);
        Random random = new Random(47);
        byte[] bytes = content.getBytes("UTF-8");
        for (int i = 0; i < 100; i++){
            ByteBuf byteBuf = Unpooled.buffer();
            int n = random.nextInt(3)+1;
            byte[] bytes1 = ("第" + i +"次发送").getBytes("UTF-8");
            byteBuf.writeChar(VERSION);
            byteBuf.writeInt(n * bytes.length + bytes1.length);// 长度
            byteBuf.writeInt(MAGICAL);
            byteBuf.writeBytes(bytes1);
            for(int k = 0; k < n; k++){
                byteBuf.writeBytes(bytes);
            }
            channel.writeInbound(byteBuf);
        }
    }


}
