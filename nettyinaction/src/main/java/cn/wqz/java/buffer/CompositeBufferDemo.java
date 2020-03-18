package cn.wqz.java.buffer;

import java.nio.ByteBuffer;

public class CompositeBufferDemo {
    public static void main(String[] args) {
        ByteBuffer header = ByteBuffer.allocate(8);
        header.put("header".getBytes());
        header.flip();
        System.out.println(header);
        ByteBuffer body = ByteBuffer.allocateDirect(16);
        body.put("body".getBytes());
        body.flip();
        System.out.println(body);
        System.out.println(header.remaining() + " " + body.remaining());
        ByteBuffer message = ByteBuffer.allocate(header.remaining() + body.remaining());
        System.out.println(message);
        message.put(header);
        header.flip();
        message.put(body);
        body.flip();
        message.flip();
        System.out.println(message);

        System.out.println(" ====================== ");
        System.out.println(getString(header));
        System.out.println(getString(body));
        System.out.println(getString(message));
    }

    public static String getString(ByteBuffer byteBuffer){
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);
        return new String(bytes);
    }

}
