package cn.wqz.netty.coder.decoder.messagetomessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.Assert;

import java.util.List;

import static org.junit.Assert.*;

/**
 * 缓冲区尺寸限制解码器
 * 缓冲区需要进行阈值限制，否则后果不堪设想
 */
public class SafeByteToMessageDecoder extends ByteToMessageDecoder {

    // 设置阈值
    private static final int MAX_FRAME_SIZE = 1024;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("decode: " + byteBuf);
        int readable = byteBuf.readableBytes();
        if(readable > MAX_FRAME_SIZE){ // 可读字节数大于阈值，则抛出异常
            byteBuf.skipBytes(readable);
            throw new TooLongFrameException("frame too big");

        }
        // 只存储阈值以内长度的数据
        list.add(byteBuf.readBytes(readable).toString());
    }

    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.buffer(2048);
        byteBuf.writerIndex(2048);
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new SafeByteToMessageDecoder());

        embeddedChannel.writeInbound(byteBuf.readBytes(1000));
        try{
            embeddedChannel.writeInbound(byteBuf.readBytes(byteBuf.readableBytes()));
            Assert.fail();
        }catch (Exception e){
            //e.printStackTrace();
        }

    }
}
