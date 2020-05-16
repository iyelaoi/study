package cn.wqz.study.netty.coder.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 将分包发送过来的字符串按照head-content(长度-内容）进行解码
 */
public class StringReplayDecoder extends ReplayingDecoder<StringReplayDecoder.Status> {
    enum Status{
        PARSE_1, PARSE_2
    }

    int count= 0;
    private int len;
    private byte[] inBytes;

    public StringReplayDecoder(){
        super(Status.PARSE_1);
    }

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println(count++ + ": " + byteBuf);
        switch (state()){
            case PARSE_1:
                len = byteBuf.readInt();
                inBytes = new byte[len];
                checkpoint(Status.PARSE_2);
                break;
            case PARSE_2:
                byteBuf.readBytes(inBytes, 0, len);
                list.add(new String(inBytes, "UTF-8"));
                checkpoint(Status.PARSE_1);
                break;
            default:
                break;
        }
    }
}
