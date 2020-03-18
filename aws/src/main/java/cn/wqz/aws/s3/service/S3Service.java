package cn.wqz.aws.s3.service;

@FunctionalInterface
public interface S3Service<T> {

    T getObject(String bucketName, String objectName);

}
