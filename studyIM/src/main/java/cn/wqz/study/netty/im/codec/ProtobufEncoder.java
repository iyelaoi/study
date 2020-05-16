package cn.wqz.study.netty.im.codec;

import cn.wqz.study.netty.im.common.bean.msg.ProtoMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtobufEncoder extends MessageToByteEncoder<ProtoMsg.Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ProtoMsg.Message message, ByteBuf byteBuf) throws Exception {
        byte[] bytes = message.toByteArray();
        int len = bytes.length;
        byteBuf.writeShort(len);
        /**
         * 魔数、版本号的写入
         */
        byteBuf.writeBytes(bytes);
    }
}
