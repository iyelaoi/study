package cn.wqz.web.server;

import cn.wqz.web.server.utils.WebProperties;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WQZServer {
    public static void main(String[] args) throws IOException {
        WebProperties webProperties = WebProperties.getInstance();
        int port = Integer.parseInt(webProperties.getProperty("server.port").trim());
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("服务器启动，监听 " + port + " 端口");
        while(true){
            Socket socket = serverSocket.accept();
            System.out.println("收到来自" + socket.getRemoteSocketAddress() + "的请求！");
            HttpSession httpSession = new HttpSession(socket);
        }

    }
}
