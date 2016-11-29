/* ChatRoom <ClientMain.java>
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



import java.io.*; 
import java.net.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets; 
import javafx.geometry.Pos; 
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

import javafx.scene.control.TextField; 
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage; 


public class ClientMain extends Application{ 
	private Stage begin;
	private Stage chat;
	private BorderPane bottomPane;
	private BorderPane getUser;
	private TextArea ta;
	private BufferedReader reader;
	private PrintWriter writer;
	private String user;

	@Override // Override the start method in the Application class 
	public void start(Stage primaryStage) {
		begin = primaryStage;
		getUser = new BorderPane();
		getUser.setPadding(new Insets(5,5,5,5));
		getUser.setTop(new Label("User Prompt"));
		
		user();
		
		// Create a scene and place it in the stage 
		Scene scene = new Scene(getUser, 200, 100); 
		begin.setTitle("User"); // Set the stage title 
		begin.setScene(scene); // Place the scene in the stage 
		begin.show(); // Display the stage 
	}
	
	
	private void user(){
		/* Set text field for user name */
		TextField get_name = new TextField();
		getUser.setAlignment(get_name, Pos.CENTER);
		getUser.setCenter(get_name);
		
		/* Set a button to set info */
		Button enter = new Button("Enter");
		getUser.setBottom(enter);
		getUser.setAlignment(enter, Pos.CENTER);
		
		enter.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				// get the name and store in private string
				setUser(get_name.getText());
				
				// Setup stage
				chat = new Stage();
				
				// Panel p to hold the label and text field 
				BorderPane pane = new BorderPane(); 
				pane.setPadding(new Insets(5, 5, 5, 5)); 
				pane.setStyle("-fx-border-color: blue"); 
				Label chatty = new Label("Chat Time");
				pane.setTop(chatty);

				// Text area to display contents 
				ta = new TextArea();
				ta.setEditable(false);
				ScrollPane scroll = new ScrollPane(ta);
				scroll.setFitToWidth(true);
				pane.setCenter(scroll);
				
				
				bottomPane = new BorderPane();
				bottomPane.setPadding(new Insets(5, 5, 5, 5)); 
				// Call method to create button for send, etc.
				sendButton();
				pane.setBottom(bottomPane);
				
				
				
				// Create a scene and place it in the stage 
				Scene scene = new Scene(pane, 450, 200); 
				chat.setTitle("Chat Room"); // Set the stage title 
				chat.setScene(scene); // Place the scene in the stage 
				chat.show(); // Display the stage 
				
				
				
				try {
					setUpNetworking();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				begin.close();
			}	
		});
	}
	
	
	
	private void sendButton(){
		/* Setup instruction */
		/* Setup a field for input */
		TextField tf = new TextField();
		bottomPane.setTop(tf);
		
		
		/* Set a button to set the input */
		Button send = new Button("Send");
		bottomPane.setBottom(send);
		bottomPane.setAlignment(send, Pos.CENTER);
		
		send.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				// get the message from the text field 
				writer.println(tf.getText());
				writer.flush();
				tf.setText("");
				tf.requestFocus();
			}
		});
	}
		
	
	
	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}
	
	

	
	class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
						
						ta.appendText(message + "\n");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
	
	
	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		Socket sock = new Socket("127.0.0.1", 4242);
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		
		//send user name to server
        writer.println(user);
        writer.flush();
        
		System.out.println("Networking Established");
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
	}
	
	
	
	public static void main(String[] args) {
		launch(args);
	}


	
}


