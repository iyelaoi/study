package cn.wqz.aio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;


/**
 * 读事件处理器
 * StringBuffer 为 attachment，用于记录总包数据
 * （因为buffer大小限制，在一次事件可能读取不完操作系统缓存（ RecvQ ）中的数据）
 */
public class SocketChannelReadHandler implements CompletionHandler<Integer, StringBuffer> {

    private AsynchronousSocketChannel socketChannel;

    // 专门用于这个通道数据缓存的ByteBuffer
    private ByteBuffer byteBuffer;

    public SocketChannelReadHandler(AsynchronousSocketChannel socketChannel, ByteBuffer byteBuffer) {
        this.socketChannel = socketChannel;
        this.byteBuffer = byteBuffer;
    }

    @Override
    public void completed(Integer result, StringBuffer attachment) {

        System.out.println("read--server");
        if(result == -1){ // 客户端主动关闭了套接字
            try {
                this.socketChannel.close(); // 服务端关闭套接字
            } catch (IOException e) {
                e.printStackTrace();
            }
            return; // 终止执行
        }

        /*
         * 实际上，由于我们从Integer result知道了本次channel从操作系统获取数据总长度
         * 所以实际上，我们不需要切换成“读模式”的，但是为了保证编码的规范性，还是建议进行切换。
         *
         * 另外，无论是JAVA AIO框架还是JAVA NIO框架，都会出现“buffer的总容量”小于“当前从操作系统获取到的总数据量”，
         * 但区别是，JAVA AIO框架中，我们不需要专门考虑处理这样的情况，因为JAVA AIO框架已经帮我们做了处理（做成了多次通知）
         */
        // 客户端有数据
        this.byteBuffer.flip(); // 准备数据
        byte[] contexts = new byte[1024];
        this.byteBuffer.get(contexts, 0, result); // 读取数据
        this.byteBuffer.clear(); // 清理buffer
        try {
            // 解码收到的字节
            String nowContent = new String(contexts, 0, result, "UTF-8");
            attachment.append(nowContent); // 将本次channel读到的数据添加到attachment
            System.out.println("当前传输结果: " + attachment);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        if(attachment.indexOf("over") == -1){ // 没有收到结束标记,数据未完成
            return;
        }else { // 读到数据结束标记，进行总数据处理
            this.byteBuffer.clear();
            System.out.println("客户端发来的信息： " + attachment); // 打印总数据
            ByteBuffer sendBuffer = null;
            try {
                sendBuffer = ByteBuffer.wrap(URLEncoder.encode("你好客户端，这是服务端返回的数据", "UTF-8" ).getBytes());
                socketChannel.write(sendBuffer);
                System.out.println("server send message to client and close this channel...");
                socketChannel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        // 处理完总数据，更新数据为空
        attachment = new StringBuffer();

        // 一次监听，一次通知
        this.socketChannel.read(this.byteBuffer, attachment, this);

    }

    @Override
    public void failed(Throwable exc, StringBuffer attachment) {
        System.out.println("发现客户端异常关闭，服务器关闭TCP通道");
        try {
            this.socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
