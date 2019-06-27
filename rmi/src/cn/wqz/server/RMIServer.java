package cn.wqz.server;

import cn.wqz.remote.IRemoteImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * RMI 服务器， 默认工作在1099端口
 *
 * 服务启动前，先启动rmiregistry
 * 命令行输入： rmiregistry
 *
 */
public class RMIServer {
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        IRemoteImpl iRemote = new IRemoteImpl(); // 要发布的对象
        Naming.rebind("RMI_IRmote", iRemote); // 对象及名称绑定
        System.out.println("========== RMI_server ready ========");
    }
}
