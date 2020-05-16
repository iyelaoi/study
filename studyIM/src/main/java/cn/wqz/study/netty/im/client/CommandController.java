package cn.wqz.study.netty.im.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class CommandController {

    private NettyClient nettyClient;

    GenericFutureListener<ChannelFuture> connectedListener = (ChannelFuture channelFuture) ->{
        final EventLoopGroup eventLoopGroup = channelFuture.channel().eventLoop();
        if(!channelFuture.isSuccess()){
            log.info("connect failure, will try reconnect after 10s");
            eventLoopGroup.schedule(()->nettyClient.doConnect(),10, TimeUnit.SECONDS);
        }
    };
}
