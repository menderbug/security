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
		

		TextArea input = new TextArea("it was her habit to build up laughter out of inadequate materials");
		TextArea key = new TextArea("steinbeck");
		TextArea output = new TextArea(Vigenere.encrypt(input.getText(), key.getText()));
		input.textProperty().addListener((observable, oldValue, newValue) -> {
			if (input.getText().equals("hackerman")) {
				primaryScene.getStylesheets().add(getClass().getResource("matrix.css").toExternalForm());
				primaryStage.sizeToScene();
			}
			output.setText(Vigenere.encrypt(input.getText(), key.getText()));
		});
		key.textProperty().addListener((observable, oldValue, newValue) -> {
			output.setText(Vigenere.encrypt(input.getText(), key.getText()));
		});
		output.textProperty().addListener((observable, oldValue, newValue) -> {
			input.setText(Vigenere.decrypt(output.getText(), key.getText()));
		});
		
		board.add(new TextField("Vigenere Encryption"), 0, 0, 3, 1);
		board.add(new TextField("Text"), 0, 1);
		board.add(new TextField("Key"), 1, 1);
		board.add(new TextField("Encrypted Text"), 2, 1);
		
		board.add(input, 0, 2);
		board.add(key, 1, 2);
		board.add(output, 2, 2);
		
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
