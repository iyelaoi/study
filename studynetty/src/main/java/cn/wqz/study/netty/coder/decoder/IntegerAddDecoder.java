package cn.wqz.study.netty.coder.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 每次只读取一个整数，读取完两个整数相加后，作为解码结果
 */
public class IntegerAddDecoder extends ReplayingDecoder<IntegerAddDecoder.Status> {
    enum Status{
        PARSE_1, PARSE_2
    }
    private int count = 0;
    private int first;
    private int secend;
    public IntegerAddDecoder(){
        super(Status.PARSE_1);
    }

    /**
     * 此方法每个int调用一次，效率不敢恭维
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        switch (state()){
            case PARSE_1:
                first = byteBuf.readInt();
                // 此种设计思路和多线程间的交互很像
                checkpoint(Status.PARSE_2);
                break;
            case PARSE_2:
                secend = byteBuf.readInt();
                Integer sum  = first + secend;
                list.add(sum);
                checkpoint(Status.PARSE_1);
                break;
            default:
                break;
        }
        System.out.println(count++ + ": " + byteBuf);
    }

}
