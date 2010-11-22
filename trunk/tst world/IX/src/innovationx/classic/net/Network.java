package src.innovationx.classic.net;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import src.innovationx.classic.Server;

public class Network implements Runnable {

	/**
	 * 1000 acceptors is good.
	 */
	public int ACCEPTOR_COUNT = 50;

	public void run() {
		try {
			for (int i = 0; i < ACCEPTOR_COUNT; i ++) {
				serverConnectionAcceptorService.submit(new ServerConnectionAcceptRunnable());
			}
			listener = new ServerSocket();
			listener.bind(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), 43594), 100);
			while(true) {
				Socket s = listener.accept();
				String connectingHost = s.getInetAddress().getHostName();
				String connector = connectingHost.replace("/", "");
				if(!Server.bannedIp(connector)) {
					System.out.println("[MONITOR]: Accepted connection from " + connector);
					Server.s.playerHandler.newPlayerClient(s, connector);
				} else {
					System.out.println("[MONITOR]: Denied connection from " + connector);
				}
			}
		}
		catch(Exception e) {
		}
	}

	private static class ServerConnectionAcceptRunnable implements Runnable
	{
		@SuppressWarnings("static-access")
		public void run() {
			Socket acceptedSocket = null;
			String acceptedSocketHost = null;

			while(true) {
				synchronized(listener) {
					try {
						acceptedSocket = listener.accept();
					}
					catch(Exception e) {
					}
				}
				try {
					acceptedSocket.setTcpNoDelay(true);
					acceptedSocketHost = acceptedSocket.getInetAddress().toString();
					String connector = acceptedSocketHost.replace("/", "");
					synchronized(Server.s.playerHandler) {
						if(/*AntiCrash.addIP(connector) && */!Server.s.bannedIp(connector)) {
							System.out.println("[MONITOR]: Accepted connection from " + connector);
							Server.s.playerHandler.newPlayerClient(acceptedSocket, connector);
						} else {
							System.out.println("[MONITOR]: Denied connection from " + connector);
						}
					}
				} catch(Exception e) {
				}
			}
		}
	}

	public static ServerSocket listener = null;
	public static ExecutorService serverConnectionAcceptorService = Executors.newFixedThreadPool(10);
}