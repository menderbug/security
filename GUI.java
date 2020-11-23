import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GUI extends Application {
	
	private GridPane board = new GridPane();
	
	public static void main(String[] args) {launch(args);}

	public void start(Stage primaryStage) throws Exception {
		
		Scene primaryScene = new Scene(board);
		primaryStage.setScene(primaryScene);
		

		TextArea vigenereInput = new TextArea("it was her habit to build up laughter out of inadequate materials");
		TextArea vigenereKey = new TextArea("steinbeck");
		TextArea vigenereOutput = new TextArea(Vigenere.encrypt(vigenereInput.getText(), vigenereKey.getText()));
		vigenereInput.textProperty().addListener((observable, oldValue, newValue) -> {
			if (vigenereInput.getText().equals("hackerman")) {
				primaryScene.getStylesheets().add(getClass().getResource("matrix.css").toExternalForm());
				primaryStage.sizeToScene();
			}
			vigenereOutput.setText(Vigenere.encrypt(vigenereInput.getText(), vigenereKey.getText()));
		});
		vigenereKey.textProperty().addListener((observable, oldValue, newValue) -> {
			vigenereOutput.setText(Vigenere.encrypt(vigenereInput.getText(), vigenereKey.getText()));
		});
		vigenereOutput.textProperty().addListener((observable, oldValue, newValue) -> {
			vigenereInput.setText(Vigenere.decrypt(vigenereOutput.getText(), vigenereKey.getText()));
		});
		
		TextArea rsaInput = null;
		TextArea rsaKey = null;		//TODO
		TextArea rsaOutput = null;
		
		board.add(new TextField("Vigenere Encryption"), 0, 0, 3, 1);
		board.add(new TextField("Text"), 0, 1);
		board.add(new TextField("Key"), 1, 1);
		board.add(new TextField("Encrypted Text"), 2, 1);
		
		board.add(new TextField("RSA Encryption"), 0, 3, 3, 1);
		board.add(new TextField("Text"), 0, 4);
		board.add(new TextField("Key"), 1, 4);
		board.add(new TextField("Encrypted Text"), 2, 4);
		
		board.add(vigenereInput, 0, 2);
		board.add(vigenereKey, 1, 2);		
		board.add(vigenereOutput, 2, 2);
		
		board.add(rsaInput, 0, 5);
		board.add(rsaKey, 1, 5);	
		board.add(rsaOutput, 2, 5);
		
		board.getChildren().forEach(n -> {if (n instanceof TextField) ((TextField) n).setEditable(false);});
		board.getChildren().forEach(n -> {if (n instanceof TextArea) ((TextArea) n).setWrapText(true);;});

		
		primaryStage.setTitle("Encoder/Decoder");
		//primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryScene.getStylesheets().add(getClass().getResource("gui.css").toExternalForm());
		primaryStage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
		primaryStage.sizeToScene();
		primaryStage.show();
		
		
	}


}
