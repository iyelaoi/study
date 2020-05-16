package cn.wqz.study.netty.im.client;

import cn.wqz.study.netty.im.IMConfig;
import cn.wqz.study.netty.im.codec.ProtobufDecoder;
import cn.wqz.study.netty.im.codec.ProtobufEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClient {

    private GenericFutureListener<ChannelFuture> channelFutureGenericFutureListener;
    private Bootstrap b;
    private EventLoopGroup g;

    public void doConnect(){
        b = new Bootstrap();
        b.remoteAddress(IMConfig.IP, IMConfig.PORT);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast("decoder", new ProtobufDecoder());
                socketChannel.pipeline().addLast("encoder", new ProtobufEncoder());
            }
        });
    }
}
