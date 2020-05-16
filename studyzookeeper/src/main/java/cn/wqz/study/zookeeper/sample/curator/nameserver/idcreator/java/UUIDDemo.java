package cn.wqz.study.zookeeper.sample.curator.nameserver.idcreator.java;

import java.util.UUID;

/**
 * 16个字节，128位
 * 27ac063a-ea1b-4a00-9497-a6dca0c48a09
 * 90ebded0-f676-44ed-b14d-c1a0cb98427c
 * 3de93038-cbbf-4325-9b24-e18ad3f9ff7f
 * 将中间连接符去掉剩余32个字符
 *
 * 优点：时延低，性能高
 * 缺点：无序，太长
 *
 */
public class UUIDDemo {
    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            System.out.println(UUID.randomUUID().toString());
        }
    }
}
