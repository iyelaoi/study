package cn.wqz.web.server.resources;

import cn.wqz.web.server.utils.ExceptionHandler;

import java.io.*;


/**
 * 文件资源
 */
public class FileResource {

    private InputStream in;

    private OutputStream out;

    private File file;

    public FileResource(File file){
        this.file = file;
    }

    public FileResource(String path){
        this(new File(path));
    }

    public void close(){
        if(in != null){
            try {
                // 关闭流
                in.close();
            } catch (IOException e) {
                ExceptionHandler.handle(e);
            }
        }
        if(out != null){
            try {
                out.close();
            } catch (IOException e) {
                ExceptionHandler.handle(e);
            }
        }
    }

    public InputStream getIn() {
        if(file.exists() && file.isFile() && this.in == null){
            try {
                this.in = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                ExceptionHandler.handle(e);
            }
        }
        return in;
    }

    public OutputStream getOut() {
        if(file.exists() && file.isFile() && this.out == null){
            try {
                this.out = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                ExceptionHandler.handle(e);
            }
        }
        return out;
    }
}
