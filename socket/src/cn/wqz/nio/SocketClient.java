package cn.wqz.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class SocketClient {
    public static void main(String[] args) {
        try(Socket clientSocket = new Socket(InetAddress.getLocalHost(),9999)){
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            bufferReader.lines().forEach(s-> System.out.println("nio client receive: " + s));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
