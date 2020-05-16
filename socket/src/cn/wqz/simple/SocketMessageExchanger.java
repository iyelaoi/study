package cn.wqz.simple;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class SocketMessageExchanger implements Runnable {

    private Socket socket;

    public SocketMessageExchanger(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("接收流");
        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
            byte[] data = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(data)) != -1) {

                String receiveData = new String(data,0, len);
                System.out.println("receive: " + receiveData);
                if("byebye".equalsIgnoreCase(receiveData.trim())){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
