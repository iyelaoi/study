package cn.wqz.simple;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        String host = "127.0.0.1";
        int port = 9999;
        Socket socket = new Socket(host,port);
        OutputStream outputStream = socket.getOutputStream();
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String data = scanner.nextLine();
            System.out.println("send: " + data);
            outputStream.write(data.getBytes("UTF-8"));
            if("byebye".equalsIgnoreCase(data.trim())){
                System.out.println("socket client exit");
                break;
            }
            outputStream.flush();
        }
        scanner.close();
        outputStream.close();
    }
}
