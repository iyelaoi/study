package cn.wqz.aws.s3.service.impl;

import cn.wqz.aws.s3.service.S3Service;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.File;
import java.nio.file.Paths;

public class S3ServiceImpl implements S3Service<File> {
    private static Region region = Region.US_EAST_2;
    private static S3Client s3 = S3Client.builder().region(region).build();

    @Override
    public File getObject(String bucketName, String objectName) {
        s3.getObject(GetObjectRequest.builder().bucket(bucketName).key(objectName).build(),
                ResponseTransformer.toFile(Paths.get("errorlog.zip")));
        File file = new File("errorlog.zip");
        if(!file.exists()){
            System.out.println("file not exists!");
        }
        return file;
    }
}
