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
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

import javafx.scene.control.TextField; 
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage; 


public class ClientMain extends Application{ 
	private Stage begin;
	private Stage chat;
	public Stage people;
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
		StackPane topbox = new StackPane();
		Text prompt = new Text("UserName");
		prompt.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		topbox.getChildren().add(prompt);
		topbox.setAlignment(Pos.CENTER);
		getUser.setTop(topbox);
		getUser.setStyle("-fx-background-image: url(\"/images/username.png\");-fx-background-size: stretch; -fx-background-repeat: no-repeat;");
		
		user();
		
		// Create a scene and place it in the stage 
		Scene scene = new Scene(getUser, 280, 285); 
		begin.setTitle("User"); // Set the stage title 
		begin.setScene(scene); // Place the scene in the stage 
		begin.show(); // Display the stage 
	}
	
	
	@SuppressWarnings("static-access")
	private void user(){
		/* Set text field for user name */
		TextField get_name = new TextField();
		get_name.setMaxSize(200, 10);
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
				
				// Setup stages for chat board and people logged in board
				chat = new Stage();
				people = new Stage();
				
				
				// Panel to hold the label and text field 
				BorderPane chat_pane = new BorderPane(); 
				chat_pane.setPadding(new Insets(5, 5, 5, 5)); 
				chat_pane.setStyle("-fx-border-color: blue"); 
				Label chatty = new Label("Chat Time");
				chat_pane.setTop(chatty);
				
				
				//Panel for people in chat
				BorderPane people_pane = new BorderPane();
				people_pane.setPadding(new Insets(5, 5, 5, 5));
				people_pane.setStyle("-fx-border-color: blue");
				StackPane people_box = new StackPane();
				Text peeps = new Text("Peeps");
				peeps.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
				people_box.getChildren().add(peeps);
				people_box.setAlignment(Pos.CENTER);
				people_pane.setTop(people_box);
				

				// Text area to display contents 
				ta = new TextArea();
				ta.setEditable(false);
				ta.setWrapText(true);
				ScrollPane scroll = new ScrollPane(ta);
				scroll.setFitToWidth(true);
				chat_pane.setCenter(scroll);
				
				
				bottomPane = new BorderPane();
				bottomPane.setPadding(new Insets(5, 5, 5, 5)); 
				// Call method to create button for send, etc.
				sendButton();
				chat_pane.setBottom(bottomPane);
				
				
				
				// Create scenes and place in the stages 
				Scene scene = new Scene(chat_pane, 450, 200); 
				chat.setTitle(getUser());  
				chat.setScene(scene); 
				chat.show();
				Scene scene2 = new Scene(people_pane, 180, 300);
				people.setTitle("Peeps in the Room");
				people.setScene(scene2);
				people.show();
				
				Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
				people.setX((primScreenBounds.getWidth() - (people.getWidth()) - 30)); 
				people.setY((primScreenBounds.getHeight() - people.getHeight()) / 3);  
				
				
				
				try {
					setUpNetworking();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				begin.close();
			}	
		});
	}
	
	
	
	@SuppressWarnings("static-access")
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


