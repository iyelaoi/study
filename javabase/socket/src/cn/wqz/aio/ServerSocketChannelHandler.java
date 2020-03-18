package cn.wqz.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;


/**
 * 注册了客户端连接事件监听器，
 * 此类为该事件的处理器
 */
public class ServerSocketChannelHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {


    private AsynchronousServerSocketChannel serverSocketChannel;

    public ServerSocketChannelHandler(AsynchronousServerSocketChannel serverSocketChannel) {
        this.serverSocketChannel = serverSocketChannel;
    }


    @Override
    public void completed(AsynchronousSocketChannel result, Void attachment) {
        // 每次都要重复注册（一次注册，一次响应）
        // 但是由于“文件状态标识符”是独享的，所以不需要担心有漏掉的
        System.out.println("connected--server");
        this.serverSocketChannel.accept(attachment, this);
        ByteBuffer readBuffer = ByteBuffer.allocate(2550);
        // 注册读事件
        // 注意： 这里的实现，每个连接创建一个新的读处理器
        //异步读操作，参数的定义：第一个参数：接收缓冲区，用于异步从channel读取数据包；
        //第二个参数：异步channel携带的附件，通知回调的时候作为入参参数，这里是作为ReadCompletionHandler的入参
        //通知回调的业务handler，也就是数据从channel读到ByteBuffer完成后的回调handler，这里是ReadCompletionHandler
        result.read(readBuffer, new StringBuffer(), new SocketChannelReadHandler(result, readBuffer));

    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        System.out.println("ServerSocketChannelHandler failed!!!");
    }
}
