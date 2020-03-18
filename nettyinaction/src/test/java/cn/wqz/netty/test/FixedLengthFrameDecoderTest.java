package cn.wqz.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
import static org.junit.Assert.*;


public class FixedLengthFrameDecoderTest {

    @Test
    public void testFrameDecoded(){
        ByteBuf byteBuf = Unpooled.buffer();
        for(int i = 0; i < 9; i++){
            byteBuf.writeByte(i); // 向buffer中缓存数据
        }
        ByteBuf input = byteBuf.duplicate(); // 引用

        // 创建EmbeddedChannel, 并传入解码 handler
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        assertTrue(channel.writeInbound(input.retain())); // 将数据写入 Channel， 作为入站数据
        assertTrue(channel.finish()); // 标记channel为完成状态
        ByteBuf read = (ByteBuf)channel.readInbound(); // 从channel中读取数据

        assertEquals(byteBuf.readSlice(3), read); // 将读取的数据与byteBuf中的数据作对比
        read.release();

        read = (ByteBuf)channel.readInbound();
        assertEquals(byteBuf.readSlice(3), read);
        read.release();

        read = (ByteBuf)channel.readInbound();
        assertEquals(byteBuf.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        byteBuf.release();
    }

    @Test
    public void testFrameDecoded2(){
        ByteBuf byteBuf = Unpooled.buffer();
        for(int i = 0; i < 9; i++){
            byteBuf.writeByte(i); // 向buffer中缓存数据
        }
        ByteBuf input = byteBuf.duplicate(); // 引用

        // 创建EmbeddedChannel, 并传入解码 handler
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        // assertTrue(channel.writeInbound(input.retain())); // 将数据写入 Channel， 作为入站数据
        // 在写之后channel相关handler便可被激活，并不是调用read方法才会激活handler
        assertTrue(channel.writeInbound(input.readBytes(5))); // ?事件的触发方式（底层原理）
        //assertTrue(channel.writeInbound(input.readBytes(4)));
        assertTrue(channel.finish()); // 标记channel为完成状态
        ByteBuf read = (ByteBuf)channel.readInbound(); // 从channel中读取数据

        assertEquals(byteBuf.readSlice(3), read); // 将读取的数据与byteBuf中的数据作对比
        read.release();

/*        read = (ByteBuf)channel.readInbound();
        assertEquals(byteBuf.readSlice(3), read);
        read.release();

        read = (ByteBuf)channel.readInbound();
        assertEquals(byteBuf.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        byteBuf.release();*/
    }
}
