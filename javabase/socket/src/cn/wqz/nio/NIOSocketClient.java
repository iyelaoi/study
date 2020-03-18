package cn.wqz.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOSocketClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 9999));
        while(true){
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if(selectionKey.isConnectable()){
                    SocketChannel socketChannel1 = (SocketChannel) selectionKey.channel();
                    if(socketChannel1.isConnectionPending()){
                        socketChannel1.finishConnect();
                    }
                    socketChannel1.configureBlocking(false);
                    System.out.println("client send");
                    socketChannel1.write(ByteBuffer.wrap("client send hello!".getBytes()));
                    socketChannel1.register(selector, SelectionKey.OP_READ);
                }else if(selectionKey.isReadable()){
                    read(selectionKey);
                }

            }

        }
    }

    private static void read(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        socketChannel.read(byteBuffer);
        byteBuffer.flip();
        System.out.println("server receive :   " + new String(byteBuffer.array()));

    }
}
