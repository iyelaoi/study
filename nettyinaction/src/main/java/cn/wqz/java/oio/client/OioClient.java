package cn.wqz.java.oio.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;

public class OioClient {
    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1";
        int port = 9999;
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(host, port));
        InputStream in = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len = -1;
        while((len = in.read(bytes)) != -1){
            System.out.println(new String(bytes, 0, len, Charset.forName("UTF-8")));
        }
        socket.close();
    }
}
