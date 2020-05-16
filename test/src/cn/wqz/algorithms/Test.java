package cn.wqz.algorithms;

import java.io.*;
import java.util.*;

class A implements Serializable{
    private static A a = new A();
    private A(){
        System.out.println("A");
    }

    public static A getInstance(){
        if(a == null){
            a = new A();
        }
        return a;
    }

   private Object readResolve(){
        return a;
    }
    private void fun(){}
}


public class Test{
    public static void main(String[] args){
        String v = "3158383";
        int x = 3158383;
        Integer i = x;
        Integer j = x;


        System.out.println(x == i);
        System.out.println(i == x);
        System.out.println(j == i);
    }


}