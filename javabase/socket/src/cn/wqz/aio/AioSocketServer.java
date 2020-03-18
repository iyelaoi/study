package cn.wqz.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AioSocketServer {

    private static final Object waitObejct = new Object();

    public static void main(String[] args) throws IOException, InterruptedException {

        /*
         * 对于使用的线程池技术，我一定要多说几句
         * 1、Executors是线程池生成工具，通过这个工具我们可以很轻松的生成“固定大小的线程池”、“调度池”、“可伸缩线程数量的池”。具体请看API Doc
         * 2、当然您也可以通过ThreadPoolExecutor直接生成池。
         * 3、这个线程池是用来得到操作系统的“IO事件通知”的，不是用来进行“得到IO数据后的业务处理的”。要进行后者的操作，您可以再使用一个池（最好不要混用）
         * 4、您也可以不使用线程池（不推荐），如果决定不使用线程池，直接AsynchronousServerSocketChannel.open()就行了。
         */
        ExecutorService threadPool = Executors.newFixedThreadPool(20);
        AsynchronousChannelGroup group = AsynchronousChannelGroup.withThreadPool(threadPool);
        final AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open(group);
        serverSocketChannel.bind(new InetSocketAddress("0.0.0.0", 9999));

        // 为AsynchronousServerSocketChannel注册监听器
        serverSocketChannel.accept(null, new ServerSocketChannelHandler(serverSocketChannel));
        synchronized (waitObejct){
            waitObejct.wait();
        }
    }
}
