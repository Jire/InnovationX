package src.innovationx.classic.net;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

 /**
    *@author Harry Andreas <harrythecomputerwizkid@msn.com>
    * MultiThreader Class
    * Handles the Executors of the threaders
    */
public class MultiThreader {

	public static ScheduledExecutorService serverLogicExecutor;
	public static ThreadPoolExecutor serverThreadPool;
	static {
		System.out.println("[MULTITHREADER] Executors starting up!");
		setServerLogicExecutor(Executors.newSingleThreadScheduledExecutor());
		setServerThreadPool((ThreadPoolExecutor) Executors.newCachedThreadPool());
		System.out.println("[MULTITHREADER] Executors Running!");
	}

public static void setServerLogicExecutor(ScheduledExecutorService serverLogicExecutor) {
		MultiThreader.serverLogicExecutor = serverLogicExecutor;
	}

	public static ScheduledExecutorService getServerLogicExecutor() {
		return serverLogicExecutor;
	}

	public static void setServerThreadPool(ThreadPoolExecutor serverThreadPool) {
		MultiThreader.serverThreadPool = serverThreadPool;
	}

	public static ThreadPoolExecutor getServerThreadPool() {
		return serverThreadPool;
	}


	
	


}