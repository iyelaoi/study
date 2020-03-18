package cn.wqz.web.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * http会话
 */
public class HttpSession implements Runnable{
    private Socket socket;

    private HttpServletRequest request;

    private HttpServletResponse response;

    public HttpSession(Socket socket){
        this.socket = socket;
        new Thread(this).start();

    }


    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            request = new HttpServletRequest(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            OutputStream outputStream = socket.getOutputStream();
            response = new HttpServletResponse(request, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
