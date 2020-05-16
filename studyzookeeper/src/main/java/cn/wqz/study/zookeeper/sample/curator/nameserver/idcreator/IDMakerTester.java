package cn.wqz.study.zookeeper.sample.curator.nameserver.idcreator;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class IDMakerTester {
    @Test
    public void testMakeId(){
        IDMaker idMaker = new IDMaker();
        idMaker.init();
        String nodeName = "/test/IDMaker/ID-";
        for (int i = 0; i < 10; i++) {
            String id = idMaker.makeId(nodeName);
            System.out.println("第" + i + "创建的id：" + id);
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        idMaker.destroy();
    }

}
