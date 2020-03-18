public class NoSignal{
	public static void main(String[] args) throws Exception{
		System.out.println("this app will use -Xsr forbidden to init attach listener thread.");
		System.out.println("sleep 60 seconds.");
		Thread.sleep(60000);
		System.out.println("sleep over, app exit!!");
	}
}
