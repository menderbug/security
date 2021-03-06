package security;

import java.math.BigInteger;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GUI extends Application {
	
	private TabPane pane = new TabPane();

	public static void main(String[] args) {launch(args);}

	public void start(Stage primaryStage) throws Exception {

		Scene primaryScene = new Scene(pane);
		primaryStage.setScene(primaryScene);


		//Vigenere implementation
		TextArea vigenereInput = new TextArea("it was her habit to build up laughter out of inadequate materials");
		TextArea vigenereKey = new TextArea("steinbeck");
		TextArea vigenereOutput = new TextArea(Vigenere.encrypt(vigenereInput.getText(), vigenereKey.getText()));
		vigenereInput.textProperty().addListener((observable, oldValue, newValue) -> {
			vigenereOutput.setText(Vigenere.encrypt(vigenereInput.getText(), vigenereKey.getText()));
		});
		vigenereKey.textProperty().addListener((observable, oldValue, newValue) -> {
			vigenereOutput.setText(Vigenere.encrypt(vigenereInput.getText(), vigenereKey.getText()));
		});
		vigenereOutput.textProperty().addListener((observable, oldValue, newValue) -> {
			vigenereInput.setText(Vigenere.decrypt(vigenereOutput.getText(), vigenereKey.getText()));
		});
		

		//DES implementation
		DES des = new DES();
		TextArea desPlainIn = new TextArea("all the girls with heads inside a dream so now we live beside the pool where everything is good");
		TextArea desKeyIn = new TextArea(DES.bitsetToString(des.getKey(),64));
		TextArea desEncryptIn = new TextArea(des.encrypt(desPlainIn.getText()));
		TextArea desEncryptOut = new TextArea(desEncryptIn.getText());
		TextArea desKeyOut = new TextArea(DES.bitsetToString(des.getKey(),64));
		TextArea desPlainOut = new TextArea(desPlainIn.getText());
		Button desEncrypt = new Button("Encrypt DES");
		desEncrypt.setOnAction(event -> desEncryptIn.setText(des.encrypt(desPlainIn.getText())));
		Button desDecrypt = new Button("Decrypt DES");
		desDecrypt.setOnAction(event -> desPlainOut.setText(des.decrypt(desEncryptIn.getText(), desKeyOut.getText())));
		desKeyIn.setEditable(false);
		
		//RSA implementation
		new RSA();
		TextArea rsaInput = new TextArea("all the girls with heads inside a dream so now we live beside the pool where everything is good");
		TextArea rsaOutput = new TextArea(RSA.encrypt(rsaInput.getText()).toString());
		TextArea rsaDecrypt = new TextArea(RSA.decrypt(RSA.encrypt(rsaInput.getText())));
		rsaInput.textProperty().addListener((observable, oldValue, newValue) -> {
			rsaOutput.setText(RSA.encrypt(rsaInput.getText()).toString());
		});
		rsaOutput.textProperty().addListener((observable, oldValue, newValue) -> {
			rsaInput.setText(RSA.decrypt(new BigInteger(rsaOutput.getText())));
			rsaDecrypt.setText(RSA.decrypt(new BigInteger(rsaOutput.getText())));
		});
		rsaDecrypt.textProperty().addListener((observable, oldValue, newValue) -> {
			rsaOutput.setText(RSA.encrypt(rsaInput.getText()).toString());
		});
		
		
		//three individual tabs
		GridPane vigenerePane = new GridPane();
		GridPane desPane = new GridPane();
		GridPane rsaPane = new GridPane();
		
		
		//adding components to Vigenere pane
		vigenerePane.add(new TextField("Vigenere Encryption"), 0, 0, 4, 1);
		vigenerePane.add(new TextField("Plaintext"), 0, 1);
		vigenerePane.add(new TextField("Key"), 1, 1);
		vigenerePane.add(new TextField("Encrypted Text"), 2, 1);

		vigenerePane.add(vigenereInput, 0, 2);
		vigenerePane.add(vigenereKey, 1, 2);
		vigenerePane.add(vigenereOutput, 2, 2);

		//adding components to DES pane
		desPane.add(new TextField("DES Encryption"), 1, 0, 3, 1);
		desPane.add(new TextField("Plaintext"), 1, 1);
		desPane.add(new TextField("Key"), 2, 1);
		desPane.add(new TextField("Encrypted Text"), 3, 1);
		
		HBox encryptBox = new HBox(desEncrypt);
		encryptBox.setPadding(new Insets(20));
		HBox decryptBox = new HBox(desDecrypt);
		decryptBox.setPadding(new Insets(20));
		desPane.add(encryptBox, 0, 2);
		desPane.add(desPlainIn, 1, 2);
		desPane.add(desKeyIn, 2, 2);
		desPane.add(desEncryptIn, 3, 2);
		
		desPane.add(new TextField("Encrypted Text"), 1, 3);
		desPane.add(new TextField("Key"), 2, 3);
		desPane.add(new TextField("Plaintext"), 3, 3);
		
		desPane.add(decryptBox, 0, 4);
		desPane.add(desEncryptOut, 1, 4);
		desPane.add(desKeyOut, 2, 4);
		desPane.add(desPlainOut, 3, 4);

		//adding components to RSA pane
		rsaPane.add(new TextField("RSA Encryption"), 0, 0, 3, 1);
		rsaPane.add(new TextField("Plaintext"), 0, 1);
		rsaPane.add(new TextField("Encrypted Text"), 1, 1);
		rsaPane.add(new TextField("Decrypted Text"), 2, 1);
		TextArea rsaKey = new TextArea(RSA.getPublicKey());
		rsaPane.add(rsaKey, 0, 3, 3, 1);
		rsaKey.setStyle("-fx-font-size: 0.8em;");

		
		rsaPane.add(rsaInput, 0, 2);
		rsaPane.add(rsaOutput, 1, 2);
		rsaPane.add(rsaDecrypt, 2, 2);
		
		//adding all three panes into the tab structure
		pane.getTabs().add(new Tab("Vigenere", vigenerePane));
		pane.getTabs().add(new Tab("DES", desPane));
		pane.getTabs().add(new Tab("RSA", rsaPane));
		
		desPane.getChildren().forEach(c -> GridPane.setValignment(c, VPos.CENTER));
		
		//making text fields uneditable and text areas wrap text
		pane.getTabs().forEach(t -> {
			((GridPane) t.getContent()).getChildren().forEach(n -> {if (n instanceof TextField) ((TextField) n).setEditable(false);});
			((GridPane) t.getContent()).getChildren().forEach(n -> {if (n instanceof TextArea) ((TextArea) n).setWrapText(true);;});
		});

		primaryStage.setTitle("Encoder/Decoder");
		primaryScene.getStylesheets().add("gui.css");
		primaryStage.sizeToScene();
		primaryStage.show();


	}




}
