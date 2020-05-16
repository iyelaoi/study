package cn.wqz.study.netty.protocol;

import cn.wqz.study.netty.NettyDemoConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * 客户端向服务端发送对象
 * ProtobufEncoder将对象进行序列化
 * ProtobufVarint32LengthFieldPrepender进行二进制序列长度控制
 *
 */
public class ProtobufClient {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup);
        bootstrap.remoteAddress(NettyDemoConfig.SOCKET_SERVER_IP, NettyDemoConfig.SOCKET_SERVER_PORT);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                socketChannel.pipeline().addLast(new ProtobufEncoder());
            }
        });
        ChannelFuture channelFuture = bootstrap.connect().sync();
        Channel channel = channelFuture.channel();
        String content = "忽如一夜春风来，千树万树梨花开";
        for (int i = 0; i < 1000; i++) {
            MsgProtos.Msg msg = build(i, i + "->" + content);
            // 发送一个对象到通道中
            channel.writeAndFlush(msg);
            System.out.println("send i = " + i);
        }
        channel.flush();
        channelFuture.channel().closeFuture().sync();
        eventLoopGroup.shutdownGracefully();
    }

    public static MsgProtos.Msg build(int id, String content){
        MsgProtos.Msg.Builder builder = MsgProtos.Msg.newBuilder();
        builder.setId(id);
        builder.setContent(content);
        MsgProtos.Msg msg = builder.build();
        return msg;
    }
}
