package cn.wqz.algorithms.client;

import cn.wqz.algorithms.remote.IRemote;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * RMI 客户端
 * 需要把远程接口 和 Stub 放入类路径里面
 */
public class RMIClient {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        // rmi://127.0.0.1:1099/RMI_IRmote 查找远端对象的格式，服务端默认端口为1099
        IRemote iRemotete = (IRemote) Naming.lookup("rmi://127.0.0.1:1099/RMI_IRmote");
        System.out.println(iRemotete.remoteMethod());
    }
}
