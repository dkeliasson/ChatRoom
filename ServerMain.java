/* ChatRoom <ServerMain.java>
 * EE422C Project 7 submission by
 * <Davin Eliasson>
 * <de5877>
 * <16445>
 * <Alex Shvedov>
 * <ays298>
 * <16445>
 * Slip days used: <1>
 * Fall 2016
 */


package assignment7;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;

import assignment7.ClientMain;


public class ServerMain extends Observable {
	private Map<ClientObserver, Socket> observers = new HashMap<ClientObserver, Socket>();
	
	public static void main(String[] args) {
		try {
			new ServerMain().setUpNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		ServerSocket serverSock = new ServerSocket(4242);
		while (true) {
			Socket clientSocket = serverSock.accept();
			
			// Get user Name of client
			BufferedReader getUser = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String name = getUser.readLine();
			
			ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
			
			// Set username
			writer.setUser(name);
			System.out.println(writer.getUser());
			
			Thread t = new Thread(new ClientHandler(clientSocket));
			t.start();
			this.addObserver(writer);
			//observers.put(writer, clientSocket);
			System.out.println("Connection Made");
		}
	}
	
	
	
	class ClientHandler implements Runnable {
		private BufferedReader reader;

		public ClientHandler(Socket clientSocket) {
			Socket sock = clientSocket;
			try {
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					System.out.println("Server read "+ message);
					setChanged();
					notifyObservers(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
