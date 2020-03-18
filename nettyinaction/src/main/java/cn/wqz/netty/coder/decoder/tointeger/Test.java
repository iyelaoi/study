package cn.wqz.netty.coder.decoder.tointeger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;

import static org.junit.Assert.*;

public class Test {
    public static void main(String[] args) {

//        ChannelInboundHandler channelInboundHandler = new ToIntegerDecoder();
        ChannelInboundHandler channelInboundHandler = new ToIntegerDecoder2();


        EmbeddedChannel embeddedChannel = new EmbeddedChannel(channelInboundHandler);
        ByteBuf byteBuf = Unpooled.buffer();
        for (int i = 0; i < 10; i++){
            byteBuf.writeByte(i);
        }
        ByteBuf data = byteBuf.duplicate();
        embeddedChannel.writeInbound(data.readBytes(8));
        try{
            embeddedChannel.writeInbound(data.readBytes(2));
            //Assert.fail(); // 如果上面没有异常， 则会到达此行，并且测试失败
        }catch (Exception e){
            System.out.println("write error");
        }
        assertTrue(embeddedChannel.finish());

        int read = embeddedChannel.readInbound();
        assertEquals(byteBuf.readSlice(4).readInt(), read);
        System.out.println("1: " + read);

        read = embeddedChannel.readInbound();
        assertEquals(byteBuf.readSlice(4).readInt(), read);
        System.out.println("2: " + read);

        read = embeddedChannel.readInbound();
        assertEquals(byteBuf.readSlice(4).readInt(), read);
        System.out.println("3: " + read);

        read = embeddedChannel.readInbound();
        assertNull(read);
        byteBuf.release();

    }
}
