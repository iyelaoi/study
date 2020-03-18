package cn.wqz.file;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Channel {

    public static void main(String[] args) {
        new Channel("1.txt");
    }

    public Channel(){

    }

    public Channel(String path){
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(path,"rw");
            FileChannel fileChannel = randomAccessFile.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            System.out.println("position:" + buffer.position());
            System.out.println("limit: " + buffer.limit());
            System.out.println("capacity: " + buffer.capacity());
            System.out.println("size: " + fileChannel.size());

            fileChannel.read(buffer);
            System.out.println("position:" + buffer.position());
            System.out.println("limit: " + buffer.limit());
            System.out.println("capacity: " + buffer.capacity());
            System.out.println("size: " + fileChannel.size());
            buffer.flip(); // 将 limit 置为position， position 置0
            fileChannel.write(buffer);
            buffer.clear();
            buffer.put("你好".getBytes());
            buffer.flip();
            fileChannel.write(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read(){

    }

    public void write(){

    }
}
