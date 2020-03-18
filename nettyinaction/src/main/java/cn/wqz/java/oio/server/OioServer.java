package cn.wqz.java.oio.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OioServer {
    public static void main(String[] args) throws IOException {
        int port = 9999;
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(port));
        ExecutorService executorService = Executors.newCachedThreadPool();
        while(true){
            System.out.println("server accept ... ");
            Socket socket = serverSocket.accept();
            System.out.println("server accept a connection from " + socket.getRemoteSocketAddress());
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("run");
                    OutputStream outputStream;
                    try {
                         outputStream = socket.getOutputStream();
                         outputStream.write("hi, this is message from server".getBytes(Charset.forName("UTF-8")));
                         socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        try{
                            if (socket != null){
                                socket.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

        }
    }
}
