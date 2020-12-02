package security;

import java.util.BitSet;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GUI extends Application {

	private TabPane pane = new TabPane();

	public static void main(String[] args) {launch(args);}

	public void start(Stage primaryStage) throws Exception {

		DES des = new DES();
		RSA rsa = new RSA();
		Scene primaryScene = new Scene(pane);
		primaryStage.setScene(primaryScene);


		TextArea vigenereInput = new TextArea("it was her habit to build up laughter out of inadequate materials");
		TextArea vigenereKey = new TextArea("steinbeck");
		TextArea vigenereOutput = new TextArea(Vigenere.encrypt(vigenereInput.getText(), vigenereKey.getText()));
		vigenereInput.textProperty().addListener((observable, oldValue, newValue) -> {
			if (vigenereInput.getText().equals("hackerman")) {
				primaryScene.getStylesheets().add("matrix.css");
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
		TextArea desInput = new TextArea("all the girls with heads inside a dream so now we live beside the pool where everything is good");
		TextArea desKey = new TextArea(des.keyToNums());
		BitSet encryptedtext  = des.encrypt(desInput.getText());
		TextArea desEncryptedText = new TextArea(des.bitsetToString(encryptedtext,encryptedtext.length()));
		TextArea decryptedtext = new TextArea(des.decrypt(DES.bitsetToString(encryptedtext, encryptedtext.length()),desKey.getText()));
		
		TextArea rsaInput = new TextArea("all the girls with heads inside a dream so now we live beside the pool where everything is good");
		TextArea rsaKey = new TextArea(RSA.getPublicKey());
		TextArea rsaOutput = new TextArea(RSA.encrypt(rsaInput.getText()).toString());

		GridPane vigenerePane = new GridPane();
		GridPane desPane = new GridPane();
		GridPane rsaPane = new GridPane();
		

		vigenerePane.add(new TextField("Vigenere Encryption"), 0, 0, 4, 1);
		vigenerePane.add(new TextField("Text"), 0, 1);
		vigenerePane.add(new TextField("Key"), 1, 1);
		vigenerePane.add(new TextField("Encrypted Text"), 2, 1);

		desPane.add(new TextField("DES Encryption"), 0, 0, 5, 1);
		desPane.add(new TextField("Text"), 0, 1);
		desPane.add(new TextField("Key"), 1, 1);
		desPane.add(new TextField("Encrypted Text"), 2, 1);
		desPane.add(new TextField("Decrypted Text"), 0, 3);

		rsaPane.add(new TextField("RSA Encryption"), 0, 0, 3, 1);
		rsaPane.add(new TextField("Text"), 0, 1);
		rsaPane.add(new TextField("Key"), 1, 1);
		rsaPane.add(new TextField("Encrypted Text"), 2, 1);

		vigenerePane.add(vigenereInput, 0, 2);
		vigenerePane.add(vigenereKey, 1, 2);
		vigenerePane.add(vigenereOutput, 2, 2);

		desPane.add(desInput, 0, 2);
		desPane.add(desKey, 1, 2);
		desPane.add(desEncryptedText, 2, 2);
		desPane.add(decryptedtext, 0, 4);

		rsaPane.add(rsaInput, 0, 2);
		rsaPane.add(rsaKey, 1, 2);
		rsaPane.add(rsaOutput, 2, 2);
		
		pane.getTabs().add(new Tab("Vigenere", vigenerePane));
		pane.getTabs().add(new Tab("DES", desPane));
		pane.getTabs().add(new Tab("RSA", rsaPane));
		
		pane.getTabs().forEach(t -> {
			((GridPane) t.getContent()).getChildren().forEach(n -> {if (n instanceof TextField) ((TextField) n).setEditable(false);});
			((GridPane) t.getContent()).getChildren().forEach(n -> {if (n instanceof TextArea) ((TextArea) n).setWrapText(true);;});
		});

		primaryStage.setTitle("Encoder/Decoder");
		//primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryScene.getStylesheets().add("gui.css");
		primaryStage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
		primaryStage.sizeToScene();
		primaryStage.show();


	}


}
