package cn.wqz;

/**
 * 车站类，实现车票业务的具体类
 */
public class Station implements  TicketService {

    @Override
    public void sellTicket() {
        System.out.println("售票。。。");
    }

    @Override
    public void inquire() {
        System.out.println("询问。。。");
    }

    @Override
    public void withdraw() {
        System.out.println("退票。。。");
    }
}
