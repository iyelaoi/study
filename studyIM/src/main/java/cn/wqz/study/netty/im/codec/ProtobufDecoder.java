package cn.wqz.study.netty.im.codec;

import cn.wqz.study.netty.im.common.bean.msg.ProtoMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 自定义protobuf解码器
 * 处理粘包半包问题
 */
public class ProtobufDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byteBuf.markReaderIndex();
        if(byteBuf.readableBytes() < 2){
            return;
        }
        int len = byteBuf.readShort();
        if(len < 0){
            channelHandlerContext.close();
        }
        if (len > byteBuf.readableBytes()){
            byteBuf.resetReaderIndex();
            return;
        }
        /**
         * 省略魔数、版本号处理
         */
        byte[] array;
        if(byteBuf.hasArray()){ // 堆缓冲区，能够直接获取数组
            ByteBuf slice = byteBuf.slice();
            array = slice.array();
        }else{ // 直接内存缓冲区，不能够直接获取数组
            array = new byte[len];
            byteBuf.readBytes(array);
        }

        ProtoMsg.Message msg = ProtoMsg.Message.parseFrom(array);
        if(msg != null){
            list.add(msg);
        }
    }
}
