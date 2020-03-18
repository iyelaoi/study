package cn.wqz.netty.buf;

import io.netty.buffer.*;
import io.netty.util.ByteProcessor;
import io.netty.util.CharsetUtil;

public class CompositeByteBufDemo {
    public static void main(String[] args) {
        // 获取 ByteBuf 实例， 支撑数组
        ByteBuf header = Unpooled.copiedBuffer("header".getBytes());
        ByteBuf body = Unpooled.copiedBuffer("body".getBytes());

        // 组合缓冲区
        CompositeByteBuf message = Unpooled.compositeBuffer();
        message.addComponents(header, body);
        System.out.println(message);
        // 遍历组合缓冲区
        for(ByteBuf buf : message){ // 循环遍历其中的buffer
            // ByteBuf转String
            System.out.println(buf.toString(CharsetUtil.UTF_8));
        }

        System.out.println(message);
        // message 可能不支持访问其支撑数组
        if(!message.hasArray()){
            // 不支持支撑数组的缓冲访问方式
            // 获取缓冲区可读字节数
            byte[] array = new byte[message.readableBytes()];
            // 获取缓冲区字节
            message.getBytes(message.readerIndex(),array);
            System.out.println(new String(array, 0, array.length));
            System.out.println(message);
        }
        // 随机访问
        for(int i = 0; i < header.capacity(); i++){
            System.out.println((char)header.getByte(i));
        }
        // ByteBufUtil，打印十六禁止数
        System.out.println(ByteBufUtil.hexDump(header));
        System.out.println(header.indexOf(0, header.writerIndex(), (byte)'e'));
        System.out.println(header.forEachByte(new ByteProcessor.IndexOfProcessor((byte)'e')));

        System.out.println(" --------------- ");
        System.out.println((char) header.readByte());
        System.out.println((char) header.readByte());
        // 丢弃可丢弃的字节（已读字节）
        header.discardReadBytes();
        System.out.println(header);





    }
}
