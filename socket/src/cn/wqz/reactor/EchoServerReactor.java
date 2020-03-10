package cn.wqz.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;


/**
 * 单线程数据回显服务
 */
public class EchoServerReactor implements Runnable {

    Selector selector;
    ServerSocketChannel serverSocketChannel;

    public EchoServerReactor() throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(9000));
        SelectionKey sk = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new AcceptorHandler());

    }

    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey sk = iterator.next();
                    dispatch(sk);
                    iterator.remove();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void dispatch(SelectionKey sk){
        Runnable handler = (Runnable)sk.attachment();
        if(handler != null){
            handler.run();
        }

    }

    class AcceptorHandler implements Runnable{
        @Override
        public void run() {
            try {
                // 此处注意： 在Accept事件发生时调用accept方法，accept是个阻塞方法，和read及write相同
                SocketChannel socketChannel = serverSocketChannel.accept();
                if(socketChannel != null){
                    // 此处需要多次创建Handler，对于同一个连接的多次事件会多次创建与清理handler，性能将大打折扣
                    new EchoHandler(selector, socketChannel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException{
        new Thread(new EchoServerReactor()).start();
    }
}

/**
 * 回显处理器
 */
class EchoHandler implements Runnable{
    final SocketChannel channel;
    final SelectionKey sk;
    final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    static final int RECEIVING = 0, SENDING = 1;
    int state = RECEIVING;

    public EchoHandler(Selector selector, SocketChannel channel) throws IOException {
        this.channel = channel;
        channel.configureBlocking(false);
        // 先获取到选择键
        sk = channel.register(selector, 0);
        // 添加附件
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        try{
            if(state == SENDING){
                channel.write(byteBuffer);
                byteBuffer.clear();
                sk.interestOps(SelectionKey.OP_READ);
                state = RECEIVING;
            }else if(state == RECEIVING){
                int len = 0;
                while((len = channel.read(byteBuffer)) > 0){
                    System.out.println(new String(byteBuffer.array(), 0, len));
                }
                byteBuffer.flip();
                sk.interestOps(SelectionKey.OP_WRITE);
                state = SENDING;
            }
            //  此处不能关闭selectkey， 需要重复使用？？？？？为什么呢
            // 因为在selector的循环遍历，该链接的多次事件发生时，使用同一个select key，该次处理完成后续可能还会使用
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
