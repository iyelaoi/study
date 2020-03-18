package cn.wqz.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbsIntegerEncoderTest {

    @Test
    public void testEncode(){
        ByteBuf buf = Unpooled.buffer();
        for(int i = 0; i < 10; i++){
            buf.writeInt(i);
        }

        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());
        assertTrue(channel.writeOutbound(buf));
        assertTrue(channel.finish());
        for(int i = 0; i < 10; i++){
            assertEquals(i, (int)channel.readOutbound());
        }
        assertNull(channel.readOutbound());
    }

}
