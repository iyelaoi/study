package cn.wqz.aio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SockerServer {
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                AsynchronousChannelGroup group = null;
                try{
                    group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(4));
                    AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open(group)
                            .bind(new InetSocketAddress(InetAddress.getLocalHost(),9999));
                    server.accept(null, new CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>() {

                        @Override
                        public void completed(AsynchronousSocketChannel result, AsynchronousServerSocketChannel attachment) {
                            server.accept(null, this);
                            try{
                                Future<Integer> f = result.write(Charset.defaultCharset().encode("你好，wqz"));
                                f.get();
                                System.out.println("aio server send time : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                        .format(new Date()));
                                result.close();
                            }catch(InterruptedException | ExecutionException | IOException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {

                        }
                    });
                    group.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
                }catch(IOException | InterruptedException e){
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }
}
