package security;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//For image display purposes
import java.awt.Container;
import java.awt.EventQueue;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class RSA {

	private BigInteger p;
	private BigInteger q;
	private BigInteger n;
	private BigInteger phi;
	private BigInteger e;
	private BigInteger d; // private key
	
	
	private int keyLength = 1024; 
	private Random random;
	
   public RSA() {
		
		random = new Random();
		p = BigInteger.probablePrime(keyLength, random); 
		q = BigInteger.probablePrime(keyLength, random);
		n = p.multiply(q); 
		phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)); //phi(n) = (p–1)(q–1)
		
		e = new BigInteger ("65537"); // TO DO: e < n such that e is relatively prime to phi(n)
		
		//ed mod phi(n) = 1
		d = e.modInverse(phi); 
  	}	
	
	public static void main(String[] args) throws IOException {
		
		RSA test = new RSA();
		
		String plainText = "HELLO";
		byte[] bytes = plainText.getBytes();
		BigInteger message = new BigInteger(bytes);
		
		BigInteger encrypt = test.encrypt(message);
		BigInteger decrypt = test.decrypt(encrypt);
		
		System.out.println("message in plaintext: " + new String(message.toByteArray()));
		System.out.println("encrypted message: " + encrypt);
		System.out.println("decrypted message in plaintext: "+ new String(decrypt.toByteArray()));
		
		//File i/o for the duck and the encrypted image
		FileInputStream originalImage =  new FileInputStream("happyduck.jpg");
		FileOutputStream encryptedImageFile = new FileOutputStream("encryptedhappyduck.jpg");
		byte[] buffer = new byte[256];
		
		
		//Testing whether or not the BigIntegers are equal to each other
		//TODO delete "test" after figuring wtf is going on with the encryption
		while(originalImage.read(buffer) != -1) {
			BigInteger testOrig = new BigInteger(buffer);
			BigInteger testEnc = test.encrypt(testOrig);
			BigInteger testDec = test.decrypt(testEnc);
			
			System.out.println(testOrig.equals(testDec));
			
			encryptedImageFile.write(test.encrypt(new BigInteger(buffer)).toByteArray());
			
		}
		originalImage.close();
		encryptedImageFile.close();
		
		
		/* Commented out until RSA is actually fixed
		//Decrypt the file
		FileInputStream encryptedImage = new FileInputStream("encryptedhappyduck.jpg");
		FileOutputStream decryptedImageFile = new FileOutputStream("decryptedhappyduck.jpg");
		
		byte[] inputBuffer = new byte[256];
		while(encryptedImage.read(inputBuffer) != -1) {
			decryptedImageFile.write(test.decrypt(new BigInteger(inputBuffer)).toByteArray());
		}
		decryptedImageFile.close();
		encryptedImage.close();
		
		
		//Display the original
		EventQueue.invokeLater(() -> {
            DisplayImage ex = test.new DisplayImage("happyduck.jpg", "original");
            ex.setVisible(true);
        });
		
		//Display the encrypted image
		EventQueue.invokeLater(() -> {
            DisplayImage ex = test.new DisplayImage("encryptedhappyduck.jpg", "encrypted");
            ex.setVisible(true);
        });
		
		//Display the decrypted image
		EventQueue.invokeLater(() -> {
            DisplayImage ex = test.new DisplayImage("decryptedhappyduck.jpg", "decrypted");
            ex.setVisible(true);
        });
		*/
		
		
	}

	
	public BigInteger encrypt(BigInteger message) {
		return  message.modPow(e,n); // (e,n) is the public key
		
	}
	
	public BigInteger decrypt(BigInteger message) {
		return message.modPow(d,n);
		
	}
	
	/* Nested class for testing only */
	@SuppressWarnings("serial")
	private class DisplayImage extends JFrame {
		public DisplayImage(String path, String title) {

	        initUI(path, title);
	    }

	    private void initUI(String path, String title) {       
	        
	        ImageIcon ii = loadImage(path);

	        JLabel label = new JLabel(ii);

	        createLayout(label);

	        setTitle(title);
	        setLocationRelativeTo(null);
	        setDefaultCloseOperation(EXIT_ON_CLOSE);
	    }

	    private ImageIcon loadImage(String path) {

	        ImageIcon ii = new ImageIcon(path);
	        return ii;
	    }

	    private void createLayout(JComponent... arg) {

	        Container pane = getContentPane();
	        GroupLayout gl = new GroupLayout(pane);
	        pane.setLayout(gl);

	        gl.setAutoCreateContainerGaps(true);

	        gl.setHorizontalGroup(gl.createSequentialGroup()
	                .addComponent(arg[0])
	        );

	        gl.setVerticalGroup(gl.createParallelGroup()
	                .addComponent(arg[0])
	        );

	        pack();
	    }
		
	}

}
