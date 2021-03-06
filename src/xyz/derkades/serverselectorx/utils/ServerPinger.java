package xyz.derkades.serverselectorx.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.net.UnknownHostException;

import net.md_5.bungee.api.ChatColor;

public class ServerPinger {
	
	/**
	 * Pings a server. Thanks to Xexode on the spigot forums for this! (Some modifications have been made)
	 * 
	 * @return A string array containing the following information:
	 *         <ul>
	 *         <li>MOTD</li>
	 *         <li>Players online</li>
	 *         <li>Max player count</li>
	 *         </ul>
	 *         Or null if the server is not reachable within timeout
	 * @throws PingException 
	 */
	public static String[] pingServer(String ip, int port, int timeout) throws PingException {
		Socket socket = null;
		try {
			socket = new Socket(ip, port);
			socket.setSoTimeout(timeout);

			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			DataInputStream in = new DataInputStream(socket.getInputStream());

			out.write(0xFE);

			int b;
			StringBuffer str = new StringBuffer();
			while ((b = in.read()) != -1) {
				if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24) {
					str.append((char) b);
				}
			}

			String[] data = str.toString().split(ChatColor.COLOR_CHAR + "");

			if (data == null){
				throw new PingException("Data returned == null");
			}
			
			return data;
		} catch (UnknownHostException e) {
			throw new PingException(e.getMessage());
		} catch (InterruptedIOException e){
			throw new PingException(e.getMessage());
		} catch (IOException e){
			throw new PingException(e.getMessage());
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				throw new PingException(e.getMessage());
			}
		}
	}
	
	public static class PingException extends Exception {

		public PingException(String message) {
			super(message);
		}

		private static final long serialVersionUID = 5694501675795361821L;
		
	}


}
