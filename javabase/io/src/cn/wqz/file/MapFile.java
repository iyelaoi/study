package cn.wqz.file;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class MapFile {
    public static void main(String[] args) throws IOException {
        File file = new File("1.txt");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE,0,fileChannel.size());
        System.out.println(mappedByteBuffer.position());
        System.out.println(mappedByteBuffer.limit());
        System.out.println(mappedByteBuffer.capacity());
        System.out.println(mappedByteBuffer.remaining());
        CharBuffer charBuffer = Charset.forName("UTF-8").decode(mappedByteBuffer);
        System.out.println(charBuffer.toString());
        System.out.println(mappedByteBuffer.position());
        System.out.println(mappedByteBuffer.limit());
        System.out.println(mappedByteBuffer.capacity());
        System.out.println(mappedByteBuffer.remaining());

    }
}
