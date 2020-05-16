package cn.wqz.study.netty.protocol;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProtobufDemo {

    public static MsgProtos.Msg buildMsg(){
        // 目标生成类Msg中6
        MsgProtos.Msg.Builder personBuilder = MsgProtos.Msg.newBuilder();
        personBuilder.setId(1000);
        personBuilder.setContent("枯藤老树昏鸦，小桥流水人家");
        MsgProtos.Msg message = personBuilder.build();
        return message;
    }

    /**
     * 序列化为二进制字节数组
     * @throws IOException
     */
    @Test
    public void serAndDeser() throws IOException {
        MsgProtos.Msg message = buildMsg();
        // 进行序列化
        byte[] data = message.toByteArray();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(data);
        data = byteArrayOutputStream.toByteArray();
        MsgProtos.Msg inMsg = MsgProtos.Msg.parseFrom(data);
        System.out.println("id = " + inMsg.getId());
        System.out.println("content = " + inMsg.getContent());
    }

    /**
     * 序列化到二进制流
     * @throws IOException
     */
    @Test
    public void serAndDeser2() throws IOException {
        MsgProtos.Msg message = buildMsg();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        message.writeTo(byteArrayOutputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        MsgProtos.Msg inMsg = MsgProtos.Msg.parseFrom(byteArrayInputStream);
        System.out.println("id = " + inMsg.getId());
        System.out.println("content = " + inMsg.getContent());
    }

    /**
     * 序列化为head-content格式
     * 用于解决半包、粘包问题
     * @throws IOException
     */
    @Test
    public void serAndDeser3() throws IOException {
        MsgProtos.Msg message = buildMsg();
        // 进行序列化
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        message.writeDelimitedTo(byteArrayOutputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        MsgProtos.Msg inMsg = MsgProtos.Msg.parseDelimitedFrom(byteArrayInputStream);
        System.out.println("id = " + inMsg.getId());
        System.out.println("content = " + inMsg.getContent());
    }
}
