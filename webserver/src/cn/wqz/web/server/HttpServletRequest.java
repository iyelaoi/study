package cn.wqz.web.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpServletRequest {
    private String uri;
    private String method;
    private String protocal;
    private Map<String, String> parmas = new HashMap<>();


    public HttpServletRequest(InputStream inputStream){
        try {
            init(new BufferedReader(new InputStreamReader(inputStream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String addAttribute(String key, String value){
        return parmas.put(key, value);
    }

    public String getAttribute(String key){
        return parmas.get(key);
    }

    public void init(BufferedReader bufferedReader) throws IOException {

        System.out.println("socket package: ");
        // GET /haha HTTP/1.1
        String line = bufferedReader.readLine().trim();
        String[] temps = line.split(" ");
        this.method = temps[0];
        this.protocal = temps[2];
        this.uri = null;
        if(temps[1].contains("?")){
            String[] tts = temps[1].split("\\?");
            this.uri = tts[0];
            String[] ps = tts[1].split("&");
            for(String s : ps){
                String[] ss = s.split("=");
                String key = ss[0].trim();
                String value = ss[1].trim();
                addAttribute(key, value);
            }
        }else{
            this.uri = temps[1];
        }
        while((line = bufferedReader.readLine()) != null){
            System.out.println(line);
        }


    }

    public Map<String, String> getParmas() {
        return parmas;
    }

    public String getProtocal() {
        return protocal;
    }

    public String getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }
}
