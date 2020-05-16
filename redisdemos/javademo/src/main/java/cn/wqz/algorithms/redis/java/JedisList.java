package cn.wqz.algorithms.redis.java;

import redis.clients.jedis.Jedis;

import java.util.List;

/**
 *
 */
public class JedisList {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");

        jedis.lpush("ll", "1", "2", "3");
        System.out.println("=========== lpush over ============");

        long len = jedis.llen("ll");
        System.out.println("ll len: " + len);

        List<String> list = jedis.lrange("ll", 0, len);
        System.out.println(list);

        // 如果在取的过程中，其他应用或线程也取了怎么办
        for (int i = 0; i < len; i++) {
            System.out.println(jedis.lpop("ll"));
        }

        System.out.println(jedis.lpop("ll"));
    }
}
