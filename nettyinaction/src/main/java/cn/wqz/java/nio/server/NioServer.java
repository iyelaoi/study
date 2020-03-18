package cn.wqz.java.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(9998));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                try{
                    if(selectionKey.isAcceptable()){
                        ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = serverSocketChannel1.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector,
                                SelectionKey.OP_READ);
                        System.out.println("accepted connection from " + socketChannel);
                    }else if(selectionKey.isReadable()){
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        int len = socketChannel.read(byteBuffer);
                        byteBuffer.flip();
                        System.out.println("server received: " + new String(byteBuffer.array(),0,len));
                        byteBuffer.clear();
                    }
                }catch (Exception e){
                    selectionKey.channel().close();
                }
            }
        }


    }
}
