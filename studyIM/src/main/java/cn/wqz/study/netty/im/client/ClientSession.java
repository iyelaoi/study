package cn.wqz.study.netty.im.client;

import cn.wqz.study.netty.im.common.bean.User;
import cn.wqz.study.netty.im.common.bean.msg.ProtoMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Data
public class ClientSession {
    public static final AttributeKey<ClientSession> SESSION_KEY = AttributeKey.valueOf("SESSION_KEY");

    /**
     * 客户端会话管理核心
     */
    private Channel channel;
    private User user;

    /**
     * 保存登陆后的服务端sessionId
     */
    private String sessionId;

    private boolean isConnected = false;
    private boolean isLogin = false;

    public ClientSession(Channel channel){
        this.channel = channel;
        this.sessionId = String.valueOf(-1);
        channel.attr(ClientSession.SESSION_KEY).set(this);
    }

    public static void loginSuccess(ChannelHandlerContext ctx, ProtoMsg.Message pkg){
        Channel channel = ctx.channel();
        ClientSession session = channel.attr(ClientSession.SESSION_KEY).get();
        session.setSessionId(pkg.getSessionId());
        session.setLogin(true);
        log.info("login success");
    }

    public static ClientSession getSession(ChannelHandlerContext ctx){
        Channel channel = ctx.channel();
        ClientSession session = channel.attr(ClientSession.SESSION_KEY).get();
        return session;
    }

    public String getRemoteAddress(){
        return channel.remoteAddress().toString();
    }

    public ChannelFuture writeAndFlush(Object msg){
        ChannelFuture channelFuture = channel.writeAndFlush(msg);
        return channelFuture;
    }

    public void writeAndClose(Object msg){
        ChannelFuture channelFuture = channel.writeAndFlush(msg);
        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }

    public void close(){
        isConnected = false;
        ChannelFuture channelFuture = channel.close();
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()){
                    log.error("close success");
                }
            }
        });
    }

}
