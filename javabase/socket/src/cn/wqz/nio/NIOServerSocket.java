package cn.wqz.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NIOServerSocket {


    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private static boolean run = true;
    private static Selector selector = null;

    public static void main(String[] args) throws IOException {
        startServer(9999);
        select();

    }

    /**
     * 启动socketServer
     */
    public static void startServer(int port){
        System.out.println("start socketServer: ");
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            System.out.println(serverSocketChannel);
            selector = Selector.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("SocketServer already start!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择器轮巡
     */
    public static void select(){
        if(selector != null){
            while(run){
                try {
                    selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while(iterator.hasNext()){
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();
                        if(selectionKey.isAcceptable()){
                            System.out.println("isAcceptable");
                            executorService.execute(new AcceptHandler(selectionKey));
                        }else if(selectionKey.isReadable()){
                            executorService.execute(new ReadHandler(selectionKey));
                        }else if(selectionKey.isWritable()){
                            executorService.execute(new WriteHandler(selectionKey));

                        }else if(selectionKey.isConnectable()){
                            System.out.println("isConnectable is true!");
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 处理连接任务类
     */
    static class AcceptHandler implements Runnable{

        private SelectionKey selectionKey;

        public AcceptHandler(SelectionKey selectionKey){
            this.selectionKey = selectionKey;
        }

        @Override
        public void run() {
            try {
                handle();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void handle() throws IOException {

            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) this.selectionKey.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            System.out.println("server receive connection:" + socketChannel.getRemoteAddress());
            socketChannel.configureBlocking(false);
            // 注册读事件
            socketChannel.register(selector, SelectionKey.OP_READ);
        }
    }

    /**
     * 处理读取任务类
     */
    static class ReadHandler implements Runnable{

        private SelectionKey selectionKey;

        public ReadHandler(SelectionKey selectionKey){
            this.selectionKey = selectionKey;
        }

        @Override
        public void run() {
            try {
                handle();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private void handle() throws IOException {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            socketChannel.read(byteBuffer);
            byteBuffer.flip();
            System.out.println("server receive: " + new String(byteBuffer.array(), 0, byteBuffer.limit()));
            byteBuffer.clear();
            selectionKey.interestOps(SelectionKey.OP_WRITE);
        }
    }

    /**
     * 处理写任务类：
     *
     * SelectKey注册了写事件，不在合适的时间去除掉，会一直触发写事件，因为写事件是代码触发的
     * client.register(selector, SelectionKey.OP_WRITE);
     * 或者sk.interestOps(SelectionKey.OP_WRITE)
     * 执行了这以上任一代码都会无限触发写事件，跟读事件不同，一定注意
     * nio的select()的时候，只要数据通道允许写，每次select()返回的OP_WRITE都是true。
     * 所以在nio的写数据里面，我们在每次需要写数据之前把数据放到缓冲区，并且注册OP_WRITE，
     * 对selector进行wakeup()，这样这一轮select()发现有OP_WRITE之后，将缓冲区数据写入channel，
     * 清空缓冲区，并且反注册OP_WRITE，写数据完成。
     *
     *
     * 要点一：不推荐直接写channel，而是通过缓存和attachment传入要写的数据，改变interestOps()来写数据；
     * 要点二：每个channel只对应一个SelectionKey，所以，只能改变interestOps()，不能register()和cancel()。
     */
    static class WriteHandler implements Runnable {

        private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        private SelectionKey selectionKey;

        public WriteHandler(SelectionKey selectionKey){
            this.selectionKey = selectionKey;
        }

        @Override
        public void run() {

        }

        private void handle() throws IOException {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.wrap(bufferedReader.readLine().getBytes());
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
            selectionKey.interestOps(SelectionKey.OP_READ);
        }
    }
}
