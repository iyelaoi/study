package cn.wqz;


/**
 * 车票服务接口
 */
public interface TicketService {

    /**
     * 卖票
     */
    void sellTicket();

    /**
     * 询问
     */
    void inquire();

    /**
     * 退票
     */
    void withdraw();
}
