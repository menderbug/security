package security;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GUI extends Application {

	private GridPane board = new GridPane();

	public static void main(String[] args) {launch(args);}

	public void start(Stage primaryStage) throws Exception {

		DES des = new DES();
		RSA rsa = new RSA();
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

		//DES Implementation
		TextArea DESInput = new TextArea("Hellowor");
		TextArea DESKey = new TextArea(des.keyToNums());
		String encryptedtext  = DES.bitsetToString(des.encrypt(DESInput.getText()), DESInput.getText().length() * 8);
		TextArea DESEncryptedText = new TextArea(encryptedtext);
		TextArea DESDecryptedText = new TextArea(des.decrypt(encryptedtext,DESKey.getText()));
		TextArea rsaInput = new TextArea("all the girls with heads inside a dream so now we live beside the pool where everything is good");
		TextArea rsaKey = new TextArea(RSA.getPublicKey());
		TextArea rsaOutput = new TextArea(RSA.encrypt(rsaInput.getText()).toString());


		board.add(new TextField("Vigenere Encryption"), 0, 0, 4, 1);
		board.add(new TextField("Text"), 0, 1);
		board.add(new TextField("Key"), 1, 1);
		board.add(new TextField("Encrypted Text"), 2, 1);

		board.add(new TextField("DES Encryption"), 0, 3, 5, 1);
		board.add(new TextField("Text"), 0, 4);
		board.add(new TextField("Key"), 1, 4);
		board.add(new TextField("Encrypted Text"), 2, 4);
		board.add(new TextField("Decrypted Text"), 0, 6);


		/*
		board.add(new TextField("RSA Encryption"), 0, 3, 3, 1);
		board.add(new TextField("Text"), 0, 4);
		board.add(new TextField("Key"), 1, 4);
		board.add(new TextField("Encrypted Text"), 2, 4);
		*/
		board.add(vigenereInput, 0, 2);
		board.add(vigenereKey, 1, 2);
		board.add(vigenereOutput, 2, 2);

		board.add(DESInput, 0, 5);
		board.add(DESKey, 1, 5);

		board.add(DESEncryptedText, 2, 5);
		board.add(DESDecryptedText, 0, 7);

		/*board.add(rsaInput, 0, 5);
		board.add(rsaKey, 1, 5);
		board.add(rsaOutput, 2, 5);
		*/
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
