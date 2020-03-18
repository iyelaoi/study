package cn.wqz;

/**
 * 车票代售点，车站代理类
 */
public class StationProxy implements TicketService{

    private TicketService ticketService;

    public StationProxy(TicketService ticketService){
        this.ticketService = ticketService;
    }

    @Override
    public void sellTicket() {
        System.out.println("代售");
        ticketService.sellTicket();
        System.out.println();
    }

    @Override
    public void inquire() {
        System.out.println("代理询问");
        ticketService.inquire();
        System.out.println();
    }

    @Override
    public void withdraw() {
        System.out.println("代理");
        ticketService.withdraw();
        System.out.println();
    }
}
