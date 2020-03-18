package cn.wqz.aws.s3;

import cn.wqz.aws.s3.service.S3Service;
import cn.wqz.aws.s3.service.impl.S3ServiceImpl;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Test {
    public static void main(String[] args) throws IOException {
        S3Service<File> s3Service = new S3ServiceImpl();
        String bucketName = "bucket1561510058862";
        String objectName = "errorlog.zip";
        File file = s3Service.getObject(bucketName, objectName);
        if(!file.exists()){
            return;
        }
        ZipFile zipFile = new ZipFile(file);
        Enumeration<ZipEntry> enumeration = (Enumeration<ZipEntry>)zipFile.entries();
        while(enumeration.hasMoreElements()){
            ZipEntry ze = enumeration.nextElement();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(ze)));
            String line ;
            while((line = bufferedReader.readLine()) != null){
                System.out.println(line);
            }
            bufferedReader.close();
        }
        System.out.println("========= OK ================");
    }
}
