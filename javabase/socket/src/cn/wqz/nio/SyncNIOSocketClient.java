package cn.wqz.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SyncNIOSocketClient {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1",9999));
        if(socketChannel.isConnectionPending()) {
            System.out.println("ding....");
            socketChannel.finishConnect();
        }

        ByteBuffer byteBuffer = ByteBuffer.wrap("client message".getBytes());
        socketChannel.write(byteBuffer);
        byteBuffer.clear();
        Thread.sleep(10000);
        socketChannel.close();
    }
}
