package cn.wqz.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.InetSocketAddress;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.concurrent.ThreadFactory;

public class NettyServer {

    public static void main(String[] args) throws Exception {

        // 1. 这就是主要的服务启动器
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        //=======================2. 下面我们设置线程池,两个线程组
        // a. BOSS线程池
        EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);

        // b. WORK线程池：这样的申明方式，主要是为了向读者说明Netty的线程组是怎样工作的
        ThreadFactory threadFactory = new DefaultThreadFactory("work thread pool");

        //CPU个数
        int processorsNumber = Runtime.getRuntime().availableProcessors();
        EventLoopGroup workLoogGroup = new NioEventLoopGroup(processorsNumber * 2, threadFactory, SelectorProvider.provider());

        // 3. 指定Netty的Boss线程和work线程
        serverBootstrap.group(bossLoopGroup , workLoogGroup);

        //如果是以下的申明方式，说明BOSS线程和WORK线程共享一个线程池
        //（实际上一般的情况环境下，这种共享线程池的方式已经够了）
        //serverBootstrap.group(workLoogGroup);

        //========================4. 下面我们设置我们服务的通道类型
        //只能是实现了ServerChannel接口的“服务器”通道类
        serverBootstrap.channel(NioServerSocketChannel.class);

        //当然也可以这样创建（那个SelectorProvider是不是感觉很熟悉？）
        //serverBootstrap.channelFactory(new ChannelFactory<NioServerSocketChannel>() {
        //  @Override
        //  public NioServerSocketChannel newChannel() {
        //      return new NioServerSocketChannel(SelectorProvider.provider());
        //  }
        //});

        //========================5. 设置处理器
        //为了演示，这里我们设置了一组简单的ByteArrayDecoder和ByteArrayEncoder
        //Netty的特色就在这一连串“通道水管”中的“处理器”
        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            /* (non-Javadoc)
             * @see io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)
             * ChannelInitializer的实现类必须要重写这个方法，这个方法在Channel被注册到EventLoop的时候会被调用
             */
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                System.out.println("报告");
                System.out.println("信息：有一客户端链接到本服务端");
                System.out.println("IP:" + ch.localAddress().getHostName());
                System.out.println("Port:" + ch.localAddress().getPort());
                System.out.println("报告完毕");
                ch.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));
                System.out.println("1");
                TCPServerHandler tcpServerHandler = new TCPServerHandler();
                ch.pipeline().addLast(tcpServerHandler);
                System.out.println("2");
                ch.pipeline().addLast(new ByteArrayEncoder());
                System.out.println("ok");
            }
        });

        //========================6. 设置netty服务器绑定的ip和端口
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.bind(new InetSocketAddress("0.0.0.0", 8888)).sync();
        System.out.println("server port : 8888");
        //还可以监控多个端口
        //serverBootstrap.bind(new InetSocketAddress("0.0.0.0", 84));
    }

}
