package cn.wqz.thinking.collection.bitsets;

import java.util.BitSet;

/**
 * 查找电话号码是否重复
 * 千万量级
 */
public class PhoneNum {

    public static void main(String[] args) {
        BitSet bitSet = new BitSet(1000000);
        bitSet.set(9999);
        bitSet.set(10293840);
        System.out.println(bitSet.get(9999));
        System.out.println(bitSet.get(10293840));

    }
}
