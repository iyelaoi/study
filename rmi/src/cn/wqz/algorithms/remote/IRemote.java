package cn.wqz.algorithms.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 远程接口
 * 每一个方法都要抛RemoteException异常
 */
public interface IRemote extends Remote {

    /**
     * 远程方法
     * @return
     * @throws RemoteException
     */
    String remoteMethod() throws RemoteException;

}
