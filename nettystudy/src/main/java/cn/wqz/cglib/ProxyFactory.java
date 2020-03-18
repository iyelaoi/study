package cn.wqz.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class ProxyFactory implements MethodInterceptor {

    private Object target;

    public ProxyFactory(Object target){
        this.target = target;
    }


    public Object getProxyInstance(){
        Enhancer en = new Enhancer();
        en.setSuperclass(this.target.getClass());
        en.setCallback(this);
        return en.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        System.out.println("start translation");
        Object returnValue = method.invoke(this.target, objects);
        System.out.println("submit translation");
        return returnValue;
    }
}
