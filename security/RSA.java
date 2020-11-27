package security;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class RSA {

	private static BigInteger p;
	private static BigInteger q;
	private static BigInteger modulus; //n, part of public key
	private static BigInteger phi;
	private static BigInteger e; // public key exponent
	private static BigInteger privateKey; // d
	private final int keyLength = 1024; 
	private static Random random;
	
	
   public RSA() {
		
	 //Choosing two large prime numbers p, q
	   random = new Random();
	   p = BigInteger.probablePrime(keyLength/2, random); 
	   q = BigInteger.probablePrime(keyLength/2, random);
	
	   //Creating modulus = pq such that phi(modulus) = (p-1)(q-1) 
	   // (e,modulus) is the public key
	   modulus = p.multiply(q); //n
	   phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)); 
	   e =  BigInteger.probablePrime(keyLength/2, random); 
	   e = exponentCheck(e,phi,modulus);
	   
	   //Choose d such that ed mod phi(n) = 1
	   //Use checkPrivateKey to test that this is 
	   privateKey = e.modInverse(phi); //d
  	}	
	
	public static void main(String[] args) throws IOException {
		
        RSA test = new RSA();
		
		//Testing private key and public key
        //Wasn't sure how we wanted to print out the keys so I made methods to print them individually and as a key pair
        //We can delete the ones we don't want later
		System.out.println("Public Key: " + getPublicKey());
		System.out.println("Private Key: " + getPrivateKey());
		System.out.println("Generating key pair... ");
		System.out.println(generateKeyPair()); //key pair
		System.out.println("Private key check is: " + checkPrivateKey(e,privateKey, phi));
		
		
		//Testing string encryption/decryption
		//Uses encrypt and decrypt methods
		String plainText = "meet me behind the mall";
		BigInteger encrypt = encrypt(plainText);
		String decrypt = decrypt(encrypt);
		System.out.println("original message in plaintext: " + plainText);
		System.out.println("encrypted message: " + encrypt);
		System.out.println("decrypted message in plaintext: "+ decrypt);
		
	
		//Second attempt for image encryption starts here
		//Uses getImageBytes and encryptImage methods
		byte[] originalBytes = getImageBytes("happyduck.jpg");
		String originalBytesToString = originalBytes.toString();
		System.out.println("bytes from original image: " + originalBytesToString);
		FileOutputStream encryptedImageFile = new FileOutputStream("encryptedhappyduck.jpg");
		encryptedImageFile.write(encryptImage(originalBytes));
		encryptedImageFile.close();
		
		
		/*
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
		
		*/
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
	
		
		
	}
	
	//Checks that private key is correct 
	//Should return e(privateKey)mod(phi) = 1
	public static BigInteger checkPrivateKey(BigInteger numberE, BigInteger numberD, BigInteger numberP){
		return (numberE.multiply(numberD)).mod(numberP);
	}
	
	//Chooses e < modulus such that e is relatively prime to phi(modulus)
	public static BigInteger exponentCheck(BigInteger numberE, BigInteger numberP, BigInteger numberM ){
		
		//First half: checks that modulus > e 
		//Second half: checks that  e is relatively prime to phi by checking if 1 is the gcd 
		 while (numberM.compareTo(numberE) > 0 && numberP.gcd(numberE).compareTo(BigInteger.ONE) > 0 ){ 
			   	numberE.add(BigInteger.ONE);
			   	}
		 return numberE;
	}
	
	//Returns public key in the form (e,modulus)
	public static String getPublicKey(){
		return "(" + e.toString() +" , "+ modulus.toString() + ")";
	}
	
	//Returns private key
	public static String getPrivateKey(){
		return privateKey.toString();
	}
	
	//Generates public key AND private key
	public static String generateKeyPair()
	{
		return "Public Key: " + getPublicKey() +"\n" +"Private Key: " + getPrivateKey();
	}
	
	//Encryption method for Strings
	public static BigInteger encrypt(String message){
		BigInteger messageBytes = new BigInteger(message.getBytes());
		return messageBytes.modPow(e,modulus);
		
	}
	
	//Second attempt at encrypting image
	public static byte[] encryptImage(byte[] image){
		byte [] encryptedImage = new byte[image.length];		
		
		//encrypting every single byte of image
		//This actually takes alot of time...so i'm going to try to rethink this lol
		//Possible problem: could this be a problem of how i am trying to encrypt an 8 bit input using 1024 key?
		for(int i = 0; i < image.length;i++){
			BigInteger byteToBigInteger = BigInteger.valueOf(image[i]);
			//System.out.println(byteToBigInteger.toString()); 
			encryptedImage[i] = (byteToBigInteger).modPow(e,modulus).byteValue();
			
		}
	   return encryptedImage;
	}
	
	//Decryption method for Strings
	public static String decrypt(BigInteger message){
		BigInteger decrypt = message.modPow(privateKey,modulus);
		return new String (decrypt.toByteArray());
	    
	}
	
	//Returns the bytes of image input
	public static byte[] getImageBytes(String path) throws IOException {
		BufferedImage original = ImageIO.read(new File (path));
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageIO.write(original, "jpg", output);
		
		return output.toByteArray();
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
