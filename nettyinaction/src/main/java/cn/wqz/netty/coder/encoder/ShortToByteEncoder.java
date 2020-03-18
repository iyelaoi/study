package cn.wqz.netty.coder.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器： Short 编码为 Byte
 */
public class ShortToByteEncoder extends MessageToByteEncoder<Short> {
    /**
     *
     * @param channelHandlerContext
     * @param aShort
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Short aShort, ByteBuf byteBuf) throws Exception {
        byteBuf.writeShort(aShort); // 将short输入到Buf
    }

    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(new ShortToByteEncoder());
        channel.writeOutbound((short)10);
        ByteBuf byteBuf = channel.readOutbound();
        System.out.println(byteBuf.readShort());
    }
}
