package src.innovationx.classic.model;
import java.util.ArrayList;

/**
 * Stops crashes from SYI bots and floods
 * @author Canownueasy <tgpn1996@hotmail.com>
 */

public class AntiCrash {

	/**
	 * The amount of connections per IP a user can have.
	 */
	private static short CONNECTIONS_PER_IP = 10;
	
	/**
	 * Holds elements of the connectedIPs on the server.
	 */
	public static ArrayList<String> connectedIPs = new ArrayList<String>();
	
	/**
	 * Adds and checks if an IP can be added
	 * @param IP The IP to attempt to add.
	 */
	public static boolean addIP(String IP) {
		if(connectionsFromIP(IP) > CONNECTIONS_PER_IP) {
			return false;
		}
		connectedIPs.add(IP);
		System.out.println("[MONITOR]: Accepted connection from " + IP);
		return true;
	}
	
	/**
	 * Removes an IP from the list
	 * @param IP The IP to remove.
	 */
	public static boolean removeIP(String IP) {
		if(connectedIPs.contains(IP)) {
			connectedIPs.remove(IP);
			System.out.println("[MONITOR]: Disconnected IP " + IP);
		}
		return true;
	}

	/**
	 * Amount of times the IP shows up in connectedIPs
	 * @param IP the IP to check.
	 */
	private static int connectionsFromIP(String IP) {
        	int occurences = 0;
        	for(Object i : connectedIPs.toArray()) {
                	if(i.toString().equals(IP)) {
                       		occurences++;
                	}
        	}
        	return occurences;
	}
}