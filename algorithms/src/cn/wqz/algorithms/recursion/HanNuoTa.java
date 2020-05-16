package cn.wqz.algorithms.recursion;

import java.util.Stack;


/**
 * 汉诺塔递归程序
 */
public class HanNuoTa {

    static final int n = 10;
    static Stack<Integer> stackA = new Stack<>();
    static Stack<Integer> stackB = new Stack<>();
    static Stack<Integer> stackC = new Stack<>();
    static{
        for (int i = 0; i < n; i++) {
            stackA.push(n - i);
        }
    }

    public static void main(String[] args) {
        System.out.println("移动前：");
        System.out.println(stackA);
        System.out.println(stackB);
        System.out.println(stackC);

        move(n, stackA,stackB, stackC);
        System.out.println("移动后：");
        System.out.println(stackA);
        System.out.println(stackB);
        System.out.println(stackC);
        System.out.println(" ========= 移动信息打印 =============");

        move2(5, 'A', 'B', 'C');

    }

    /**
     * 递归程序
     * @param k 处理盘子个数
     * @param A 原始盘子所在杆子
     * @param B 辅助杆子
     * @param C 目的盘子所在杆子
     */
    private static void move(int k, Stack<Integer> A, Stack<Integer> B, Stack<Integer> C){

        if(k == 1){
            /**
             * 如果只有一个，则直接将其移动到C杆
             */
            int num = A.pop();
            C.push(num);
        }else{

            /**
             * 将A杆上k-1个盘经C杆移动到B杆
             * 之后C杆为空，B杆上盘小于A杆上上的盘
             */
            move(k-1, A, C, B);

            /**
             * 将A杆上的顶盘移至C杆，作为C杆最底
             */
            int num = A.pop();
            C.push(num);

            /**
             * 将B杆k-1个盘经A杆移动到C杆
             */
            move(k-1, B, A, C); //
        }
    }

    static int count = 0;

    private static void move2(int n, char a, char b, char c){
        if(n == 1){
            System.out.println(++count + " " + n + ": " + a + " --> " + c);
            return;
        }
        move2(n-1, a, c, b);
        System.out.println(++count + " " + n + ": " + a + " --> " + c);
        move2(n-1, b, a, c);
    }

}
