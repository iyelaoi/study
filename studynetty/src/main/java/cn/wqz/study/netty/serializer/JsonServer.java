package cn.wqz.study.netty.serializer;

import cn.wqz.study.netty.serializer.entity.JsonMsg;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;

/**
 * 将对象序列化为json格式进行传输处理
 * 服务端负责Json解码和反序列化
 */
public class JsonServer {
    public static void main(String[] args) throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup g1 = new NioEventLoopGroup(1);
        EventLoopGroup g2 = new NioEventLoopGroup();
        serverBootstrap.group(g1,g2).localAddress(9000)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0 ,4, 0 ,4));
                        socketChannel.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                        socketChannel.pipeline().addLast(new JsonMsgDecoder());
                    }
                });
        serverBootstrap.bind().sync();
    }

    /**
     * 进行反序列化
     */
    static class JsonMsgDecoder extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String s = (String)msg;
            JsonMsg jsonMsg = JsonMsg.parseFromJson(s);
            System.out.println("server receive jsonmsg:" + jsonMsg);
        }
    }
}
