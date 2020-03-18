package cn.wqz.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class FrameChunkDecoderTest {

    @Test
    public void testFrameDecoder(){
        ByteBuf byteBuf = Unpooled.buffer();
        for(int i = 0; i < 9; i++){
            byteBuf.writeByte(i);
        }

        ByteBuf byteBuf1 = byteBuf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(3));
        assertTrue(channel.writeInbound(byteBuf1.readBytes(2)));
        try{
            channel.writeInbound(byteBuf1.readBytes(4));
            Assert.fail();
        }catch (TooLongFrameException e){

        }

        assertTrue(channel.writeInbound(byteBuf1.readBytes(3)));
        assertTrue(channel.finish());

        ByteBuf read = (ByteBuf)channel.readInbound();
        System.out.println("hh" + read);
        assertEquals(byteBuf.readSlice(2), read);
        read.release();

        read = (ByteBuf)channel.readInbound();
        assertEquals(byteBuf.skipBytes(4).readSlice(3), read);
        read.release();

        byteBuf.release();

    }

}
