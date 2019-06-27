package cn.wqz.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * 远程接口实现类
 * 必须继承UnicastRemoteObject
 * 使用 rmic 命令生成骨架及静态存根 Stub
 * 命令格式： rmic 包.类
 */
public class IRemoteImpl extends UnicastRemoteObject implements IRemote {

    /**
     * 构造方法必须抛出RemoteException异常
     * @throws RemoteException
     */
    public IRemoteImpl()throws RemoteException{}

    @Override
    public String remoteMethod() throws RemoteException {
        return "this is a remote method imremote.remoteMethod()";
    }
}
