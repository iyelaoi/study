package cn.wqz.algorithms.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SimpleFileIo {

    public static void main(String[] args) throws IOException {
        System.out.println(new File(".").getAbsolutePath());
        // 写字节
        Files.write(Paths.get("./file/1.txt"), "wqz".getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        // 读取字节
        System.out.println(new String(Files.readAllBytes(Paths.get("./file/1.txt")),StandardCharsets.UTF_8));
    }
}
