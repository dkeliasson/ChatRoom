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


public class ClientMain extends Application { 
	// IO streams 
	DataOutputStream toServer = null; 
	DataInputStream fromServer = null;
	
	private TextArea ta;
	
	private BufferedReader reader;
	private PrintWriter writer;


	@Override // Override the start method in the Application class 
	public void start(Stage primaryStage) { 
		// Panel p to hold the label and text field 
		BorderPane pane = new BorderPane(); 
		pane.setPadding(new Insets(5, 5, 5, 5)); 
		pane.setStyle("-fx-border-color: green"); 
		Label chat = new Label("Chat Time");
		pane.setTop(chat);

		// Text area to display contents 
		ta = new TextArea();
		ta.setEditable(false);
		ScrollPane scroll = new ScrollPane(ta);
		scroll.setFitToWidth(true);
		pane.setCenter(scroll);
		
		
		/* Setup the grid for all buttons and text fields to be placed in screen stage */
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(5);
		grid.setVgap(15);
		grid.setPadding(new Insets(10, 10, 10, 10));
		
		// Call method to create button for send, etc.
		sendButton(grid);
		
		pane.setBottom(grid);
		// Create a scene and place it in the stage 
		Scene scene = new Scene(pane, 450, 200); 
		primaryStage.setTitle("Chat Room"); // Set the stage title 
		primaryStage.setScene(scene); // Place the scene in the stage 
		primaryStage.show(); // Display the stage 
		
		
		
		try {
			setUpNetworking();
		} catch (Exception e1) {
			e1.printStackTrace();
		}


	}
	
	
	private GridPane sendButton(GridPane grid){
		/* Setup instruction */
		/* Setup a field for input */
		TextField tf = new TextField();
		GridPane.setConstraints(tf, 1, 0);
		
		/* Set a button to set the input */
		Button send = new Button("Send");
		GridPane.setConstraints(send, 2, 0);
		
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
		grid.getChildren().addAll(tf, send);
		return grid;
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
		System.out.println("networking established");
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
	}
	
	
	
	public static void main(String[] args) {
		launch(args);
	}
}


