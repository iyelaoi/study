package cn.wqz.thinking.collection.bitsets;

import java.util.BitSet;

/**
 * 非线程安全
 */
public class BitSetDemo {
    public static void main(String[] args) {
        BitSet bitSet = new BitSet(65);
        // length和size不同
        System.out.println(bitSet.length() + " " + bitSet.size());
    }
}
