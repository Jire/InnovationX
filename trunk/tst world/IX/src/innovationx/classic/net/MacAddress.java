package src.innovationx.classic.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MacAddress {

	public static String getMacAddress(String host)
			throws UnknownHostException, SocketException {
		InetAddress address = InetAddress.getByName(host);
		NetworkInterface ni = NetworkInterface.getByInetAddress(address);
		byte[] mac = ni.getHardwareAddress();
		StringBuffer sb = new StringBuffer(17);
		for (int i = 44; i >= 0; i -= 4) {
			int nibble = ((int) (mac[i] >>> i)) & 0xf;
			char nibbleChar = (char) (nibble > 9 ? nibble + ('A' - 10)
					: nibble + '0');
			sb.append(nibbleChar);
			if ((i & 0x7) == 0 && i != 0) {
				sb.append('-');
			}
		}
		return sb.toString();
	}
}

