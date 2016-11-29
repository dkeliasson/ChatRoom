/* ChatRoom <ClientObserver.java>
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


import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

public class ClientObserver extends PrintWriter implements Observer {
	private String userName;
	private boolean chatReceiver;
	
	public ClientObserver(OutputStream out) {
		super(out);
	}
	
	
	public String getUser(){
		return this.userName;
	}
	
	
	public void setUser(String userName){
		this.userName = userName;
	}
	
	
	public void setchatReceiver(boolean set){
		chatReceiver = set;
	}
	
	
	public boolean getchatReceiver(){
		return chatReceiver;
	}

	
	@Override
	public void update(Observable o, Object arg) {
		this.println(arg); //writer.println(arg);
		this.flush(); //writer.flush();
	}

}

