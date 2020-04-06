package cn.wqz.redis.java;

import redis.clients.jedis.Jedis;

public class JedisString {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");
        System.out.println(jedis.ping());
        jedis.set("user1","uuid1");
        jedis.set("user2","uuid2");
        jedis.set("user3","uuid3");
        jedis.set("user4","uuid4");

        System.out.println("insert over!!!");
        for (String key : jedis.keys("*")) {

            if (key.contains("user") &&"string".equals(jedis.type(key))){
                System.out.println(jedis.get(key));
            }
        }
    }

}
