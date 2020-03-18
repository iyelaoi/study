package cn.wqz.aio.client;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;


/**
 * 连接服务端成功
 * 事件处理类
 */
public class SocketClientConnectHandler implements CompletionHandler<Void, SocketClientConnectHandler> {

    private AsynchronousSocketChannel socketChannel;

    public SocketClientConnectHandler(AsynchronousSocketChannel socketChannel){
        this.socketChannel = socketChannel;
        System.out.println("connect construct -- client");
    }

    @Override
    public void completed(Void result, SocketClientConnectHandler attachment) {
        //

    }

    @Override
    public void failed(Throwable exc, SocketClientConnectHandler attachment) {

    }
}
