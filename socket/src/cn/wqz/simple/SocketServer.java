package cn.wqz.simple;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SocketServer {
    private static boolean flag = true;
    public static void main(String[] args) throws IOException {

        ExecutorService executorService = Executors.newCachedThreadPool();
        int port = 9999;
        ServerSocket serverSocket = new ServerSocket(port);
        while(flag){
            System.out.println("等待连接：");
            Socket socket = serverSocket.accept();
            System.out.println("accepted a socket connection");
            executorService.execute(new SocketMessageExchanger(socket));
        }
    }
}
