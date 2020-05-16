package cn.wqz.nio.select;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioDiscardServer {

    public static void startServer()throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(9000));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while(selector.select() > 0){
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while(keys.hasNext()){
                SelectionKey selectionKey = keys.next();
                if(selectionKey.isAcceptable()){
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("server received a connection : " + socketChannel.getRemoteAddress());
                }else if(selectionKey.isReadable()){
                    System.out.println("readAble");
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                    int len = 0;
                    len = socketChannel.read(buffer);
                    buffer.flip();
                    System.out.println("OK: " + len);
                    System.out.println(new String(buffer.array(),0,len));
                    buffer.clear();
                    socketChannel.close();
                }
                keys.remove();
            }
        }
    }

    public static void main(String[] args) throws IOException{
        startServer();
    }
}
