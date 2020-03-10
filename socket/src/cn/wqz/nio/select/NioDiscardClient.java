package cn.wqz.nio.select;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioDiscardClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 9000));
        while(!socketChannel.finishConnect()){

        }
        System.out.println("client connected");
        ByteBuffer buffer = ByteBuffer.wrap("hello world".getBytes());
        System.out.println(buffer);
        buffer.flip();

        System.out.println(buffer);
        buffer.limit(buffer.capacity());
        System.out.println(buffer);
        socketChannel.write(buffer);
        socketChannel.shutdownOutput();
        socketChannel.close();
    }
}
