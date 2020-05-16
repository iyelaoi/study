package cn.wqz.study.netty.protocol;

import cn.wqz.study.netty.NettyDemoConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

/**
 * 接受客户端传来的对象protobuf序列化二进制序列
 * 根据ProtobufVarint32FrameDecoder进行长度控制
 * 根据ProtobufDecoder进行反序列化，得到对象
 */
public class ProtobufServer {
    public static void main(String[] args) throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup g1 = new NioEventLoopGroup(1);
        EventLoopGroup g2 = new NioEventLoopGroup();
        serverBootstrap.group(g1,g2);
        serverBootstrap.localAddress(NettyDemoConfig.SOCKET_SERVER_PORT);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                // 长度处理，自动解决粘包半包问题，结果为完整的 ByteBuf
                socketChannel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                // 从ByteBuf中获取二进制数组，从数组反序列化为目标对象
                socketChannel.pipeline().addLast(new ProtobufDecoder(MsgProtos.Msg.getDefaultInstance()));
                // 输出目标对象
                socketChannel.pipeline().addLast(new ProtobufBussinessDecoder());
            }
        });
        ChannelFuture channelFuture = serverBootstrap.bind().sync();
        channelFuture.channel().closeFuture().sync();
        g2.shutdownGracefully();
        g1.shutdownGracefully();
    }

    static class ProtobufBussinessDecoder extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            MsgProtos.Msg protoMsg = (MsgProtos.Msg)msg;
            System.out.println("server receive protoMSG:" + protoMsg.getId() + " ->" + protoMsg.getContent());
        }
    }
}
