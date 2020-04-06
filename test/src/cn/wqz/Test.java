package cn.wqz;

import javax.lang.model.util.Elements;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        A aa = (A)new ObjectInputStream(new FileInputStream("A.out")).readObject();
        System.out.println(aa);
    }


}