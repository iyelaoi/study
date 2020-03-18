package cn.wqz.aws.s3.service;

import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * AWS S3 操作接口
 *
 * @author wqz
 */
public interface S3Operations {

    /**
     * 创建存储桶
     * @param region
     * @param bucketName
     * @return
     */
    CreateBucketResponse createBucket(String region, String bucketName);

    /**
     * 列举存储桶
     * @param region
     * @return
     */
    List<Bucket> listBuckets(String region);

    /**
     * 删除存储桶
     * @param region
     * @param bucketName
     * @return
     */
    DeleteBucketResponse deleteBucket(String region, String bucketName);

    /**
     * 向桶中添加对象
     * @param region
     * @param bucketName
     * @param objectKey
     * @param byteBuffer
     * @return
     */
    PutObjectResponse putObject(String region, String bucketName, String objectKey, ByteBuffer byteBuffer);

    /**
     * 从桶中获取对象，并存于file文件中（目前仅提供存储至文件）
     * @param region
     * @param bucketName
     * @param objectKey
     * @param file 文件完整路径，获取到的文件存储于此路径
     * @return
     */
    GetObjectResponse getObjectToFile(String region, String bucketName, String objectKey, String file);

    /**
     * 获取对象列表，手动分页方式
     * @param region
     * @param bucketName
     * @return
     */
    List<ListObjectsV2Response> listObjectV2(String region, String bucketName);

    /**
     * 从桶中返回 ListObjectsV2Iterable
     * @param region
     * @param bucketName
     * @return
     */
    ListObjectsV2Iterable listObjectsV2Paginator(String region, String bucketName);

    /**
     * 从桶中删除对象
     * @param region
     * @param bucketName
     * @param objectKey
     * @return
     */
    DeleteObjectResponse deleteObject(String region, String bucketName, String objectKey);

    CompleteMultipartUploadResponse multipartUpload(String region, String bucketName, String multiPartKey, ByteBuffer[] byteBuffers);

}
