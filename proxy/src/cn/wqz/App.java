package cn.wqz;

public class App {
    public static void main(String[] args) {
        TicketService ticketService = new Station();
        TicketService ticketProxy = new StationProxy(ticketService);
        ticketProxy.sellTicket();
        ticketProxy.inquire();
        ticketProxy.withdraw();
    }
}
