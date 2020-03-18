package cn.wqz.aio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AioSocketClient {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();

        Future<Void> future = socketChannel.connect(new InetSocketAddress("127.0.0.1", 9999));
        future.get();
        System.out.println("connected--client");
        ByteBuffer byteBuffer = ByteBuffer.wrap("hello server, this is message from client -- over".getBytes());
        socketChannel.write(byteBuffer, byteBuffer, new SocketClientWriteHandler(socketChannel));
        Thread.sleep(100000);

    }
}
