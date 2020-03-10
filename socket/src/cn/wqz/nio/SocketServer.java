package cn.wqz.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SocketServer {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 4,
                60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try(Selector selector = Selector.open();
                    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()){
                    serverSocketChannel.bind(new InetSocketAddress(InetAddress.getLocalHost(), 9999));
                    serverSocketChannel.configureBlocking(false);
                    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                    while(true){
                        selector.select(); // 阻塞， 选择器中有事件发生
                        // 获取事件集合
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        // 遍历事件 key
                        while(iterator.hasNext()){
                            SelectionKey key = iterator.next(); // 获取事件

                            // 处理事件
                            try(SocketChannel channel = ((ServerSocketChannel)key.channel()).accept()){ // 获取事件Channel
                                channel.write(Charset.defaultCharset().encode("你好，wqz")); // channel 读写
                            }
                            iterator.remove();
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
