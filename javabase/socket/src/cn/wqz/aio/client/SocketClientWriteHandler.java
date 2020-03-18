package cn.wqz.aio.client;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class SocketClientWriteHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel socketChannel;

    public SocketClientWriteHandler(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        System.out.println("write--client");
        if(attachment.hasRemaining()){
            this.socketChannel.write(attachment, attachment, this);
        }else{
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            System.out.println("register read--client");
            socketChannel.read(byteBuffer,byteBuffer,new SocketClientReadHandler());
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
