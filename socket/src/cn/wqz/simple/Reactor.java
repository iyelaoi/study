package cn.wqz.simple;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 * 此处使用Java 原生非阻塞Socket
 * 实现Handler的添加、提取及调用
 * 本例为单线程的Reactor，虽为非阻塞
 * 但所有的处理逻辑均在单个线程中执行，如果一个连接处理逻辑卡顿，将导致所有的连接都出现问题
 */
public class Reactor implements Runnable {

    Selector selector;
    ServerSocketChannel serverSocketChannel;

    public Reactor()throws Exception{
        // 1. 打开选择器
        selector = Selector.open();
        // 2. 创建server端 socket Channel
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);  // 配置为非阻塞
        // 3. 给非阻塞通道注册接收连接事件
        SelectionKey sk = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 4. 给sk添加处理器
        sk.attach(new AcceptHandler());  // 此处使用Java 原生非阻塞Socket，实现Handler的附加
    }

    @Override
    public void run() {
        try{
            while(Thread.interrupted()){
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey sk = iterator.next();
                    dispatch(sk);  // 手动实现时间的分发
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void dispatch(SelectionKey sk){
        Runnable handler = (Runnable) sk.attachment();  // 手动提取附件
        if(handler != null){
            handler.run();  // 手动调用handler
        }
    }

    class AcceptHandler implements Runnable{
        @Override
        public void run() {
            //  处理连接
        }
    }
}
