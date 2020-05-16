package cn.wqz.algorithms.sorts;

import java.util.Random;

/**
 * 数据集
 */
public class DataSet {
    private static Random random = new Random(47);
    public static int[] data = {1,4,3545,345,345324,234,234,543,654};

    public static int[] getData(int len, int max) {
        int[] data = new int[len];
        for (int i = 0; i < len; i++) {
            data[i] = random.nextInt(max);
        }
        return data;
    }
}
