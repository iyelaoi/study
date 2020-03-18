package cn.wqz.web.server;

import cn.wqz.web.server.resources.FileResource;
import cn.wqz.web.server.utils.WebProperties;

import java.io.*;

public class HttpServletResponse {


    public HttpServletResponse(HttpServletRequest request, OutputStream outputStream){
        try {
            init(request, new BufferedWriter(new OutputStreamWriter(outputStream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据和处理耦合度较高
     * @param request
     * @param out
     * @throws IOException
     */
    public void init(HttpServletRequest request, BufferedWriter out) throws IOException {
        // HTTP/1.1 200 OK\r\nContent-Type: text/html;charset=gbk\r\nContent-Length:
        String url = WebProperties.getInstance().getProperty("static.path") + request.getUri();
        FileResource fileResource = new FileResource(url);
        StringBuffer buffer = new StringBuffer();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileResource.getIn()));
        String line = null;
        while((line = bufferedReader.readLine()) != null){
            buffer.append(line);
        }
        String body = buffer.toString();
        out.write(request.getProtocal() + " 200 OK\nContent-Type: text/html;\nContent-Length: " + body.getBytes().length + "\n\n");
        out.write(body);
        out.flush();
    }
}
