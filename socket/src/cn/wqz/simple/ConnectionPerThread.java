package cn.wqz.simple;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 一个socket，使用一个线程负责
 */
public class ConnectionPerThread implements Runnable {

    @Override
    public void run() {
        try{
            ServerSocket serverSocket = new ServerSocket(9000);
            while(!Thread.interrupted()){
                Socket socket = serverSocket.accept();
                new Thread(new Handler(socket)).start();
            }
        }catch(IOException e){
            /* 处理异常 */
        }
    }


    static class Handler implements Runnable{

        private Socket socket;

        public Handler(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            /* 处理socket */
        }
    }
}
