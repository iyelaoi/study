package cn.wqz.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Channel {
    public static void main(String[] args) {
        try {
            FileChannel fileChannel = new RandomAccessFile("1.txt","rw").getChannel();
            FileChannel fileChannel1 = new RandomAccessFile("2.txt", "rw").getChannel();
            int len = -1;
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024*1024);
            while((len = fileChannel.read(byteBuffer)) != -1){
                byteBuffer.flip();
                fileChannel1.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
