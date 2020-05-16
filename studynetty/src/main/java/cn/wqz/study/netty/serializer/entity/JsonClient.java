package cn.wqz.study.netty.serializer.entity;

import cn.wqz.study.netty.NettyDemoConfig;
import cn.wqz.study.netty.train.NioServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * 客户端将对象序列化为Json字符串
 * 将字符串进行编码
 * 在进行head-content编码器处理，发送
 */
public class JsonClient {

    public void runClient() throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .remoteAddress(NettyDemoConfig.SOCKET_SERVER_IP, NettyDemoConfig.SOCKET_SERVER_PORT)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 自动编码为head-content格式
                        socketChannel.pipeline().addLast(new LengthFieldPrepender(4));
                        socketChannel.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect();
        channelFuture.addListener((ChannelFuture futureListener) -> {
            if(futureListener.isSuccess()){
                System.out.println("connect success");
            }else{
                System.out.println("connect failure");
            }
        });

        channelFuture.sync();
        Channel channel = channelFuture.channel();
        String content = "山重水复疑无路， 柳暗花明又一村。";
        for (int i = 0; i < 1000; i++) {
            JsonMsg jsonMsg = build(i, i + "->" + content);
            channel.writeAndFlush(jsonMsg.convertToJson());
            System.out.println("send jsonMsg's json： " + jsonMsg.convertToJson());
        }
        channel.flush();
        ChannelFuture closeFuture = channel.close();
        closeFuture.sync();
        eventLoopGroup.shutdownGracefully();
    }

    public JsonMsg build(int id, String content){
        JsonMsg jsonMsg = new JsonMsg();
        jsonMsg.setId(id);
        jsonMsg.setContent(content);
        return jsonMsg;
    }

    public static void main(String[] args) {
        try {
            new JsonClient().runClient();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
