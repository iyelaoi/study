package cn.wqz.socket;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class SocketClient {
    public static void main(String[] args) throws IOException {
        String ip = "192.168.1.101";
        int port = 9999;
        System.out.println("开始连接服务端！");
        Socket socket = new Socket(ip, port);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        outputStream.write("weiqizhuang".getBytes());
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] datas = new byte[1024];
        int len = inputStream.read(datas);
        System.out.println("连接已经建立！！");
        System.out.println("收到 ： " + new String(datas, 0, len));
        socket.close();
    }
}
