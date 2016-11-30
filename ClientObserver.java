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
		
		
		
		
		
		String text = arg.toString();
		int index = text.indexOf('@');
		//int index2 = text.indexOf(':');
		
		if(index == -1){
			this.println(arg);
		}
		else{
			String substring1 = ":";
			String[] parts1 = text.split(substring1);
			String txName = parts1[0];

			
			
			String substring2 = "@";
			String[] parts2 = text.split(substring2);
			String subText = parts2[0];
			String rxName = parts2[1];
			
			
			if(ServerMain.observers.containsKey(rxName)){
			
				if(rxName.equals(this.userName) || txName.equals(this.userName)){
					this.println(subText);
				}
			}
			else{
				this.println(arg);
			}
		}
		
		//this.println(arg); //writer.println(arg);
		this.flush(); //writer.flush();
	}

}

