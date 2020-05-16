package cn.wqz.reactor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", 9000));
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        Scanner scanner = new Scanner(System.in);
        new Thread(){
            @Override
            public void run() {
                try{
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while((len = inputStream.read(buffer)) > 0){
                        System.out.println(new String(buffer, 0, len));
                    }

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }.start();
        while(scanner.hasNext()){
            outputStream.write(scanner.next().getBytes());
        }
    }
}
