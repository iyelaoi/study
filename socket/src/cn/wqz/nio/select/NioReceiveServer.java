package cn.wqz.nio.select;

import sun.nio.ch.IOUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.FutureTask;

public class NioReceiveServer {
    private Charset charset = Charset.forName("UTF-8");

    static class Client{
        String fileName;
        long fileLength;
        long startTime;
        InetSocketAddress remoteAddress;
        FileChannel outChannel;

    }

    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    Map<SelectableChannel, Client> clientMap = new HashMap<>();

    public void startServer()throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocketChannel.configureBlocking(false);
        InetSocketAddress address = new InetSocketAddress(9000);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("start listening ...");
        while(selector.select() > 0){
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                if(selectionKey.isAcceptable()){
                    SocketChannel socketChannel = ((ServerSocketChannel)selectionKey.channel()).accept();
                    if(socketChannel == null){ continue; }
                    socketChannel.configureBlocking(false);
                    SelectionKey selectionKey1 = socketChannel.register(selector, SelectionKey.OP_READ);
                    Client client = new Client();
                    client.remoteAddress = (InetSocketAddress)socketChannel.getRemoteAddress();
                    clientMap.put(socketChannel, client);
                    System.out.println("client: " + client.remoteAddress + " connected successfully");
                }else if(selectionKey.isReadable()){

                }

            }

        }
    }

    private void processData(SelectionKey selectionKey)throws IOException{
        Client client = clientMap.get(selectionKey.channel());
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
        int num = 0;
        try{
            buffer.clear();
            if(null == client.fileName){
                String fileName = charset.decode(buffer).toString();
                FutureTask
            }
        }catch(){}
    }
}
