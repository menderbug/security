package security;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//For image display purposes
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Graphics2D;
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
	private static BigInteger modulus;//n, part of public key
	private static BigInteger phi;
	private static BigInteger e; // public key exponent
	private static BigInteger privateKey;
	private final int keyLength = 512;
	private static Random random;


   public RSA() {

	    //Choosing two large prime numbers p, q
	   	random = new Random();
	    p = getPrime(keyLength,random);
	    q = getPrime(keyLength,random);
	    modulus = calculateModulus(p,q); //n, part of public key
		phi = calculatePhi(p,q);

		e = getPrime(keyLength,random);
		e = exponentCheck(e, p, modulus); // public key exponent
	   //Choose d such that ed mod phi(n) = 1
	   //Use checkPrivateKey to test that this is true
	   privateKey = e.modInverse(phi); //d
   }

   //Constructor to specify a seed
   public RSA(long seed) {

	   //Choosing two large prime numbers p, q
	   random = new Random(seed);
	   p = BigInteger.probablePrime(keyLength/2, random);
	   q = BigInteger.probablePrime(keyLength/2, random);
	
	   //Creating modulus = pq such that phi(modulus) = (p-1)(q-1) 
	   // (e,modulus) is the public key
	   modulus = p.multiply(q); //n
	   phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
	   e =  BigInteger.probablePrime(keyLength/2, random);
	   e = exponentCheck(e,phi,modulus);

	   //Choose d such that ed mod phi(n) = 1
	   //Use checkPrivateKey to test that this is true
	   privateKey = e.modInverse(phi); //d
   }
   //Everything in Main is for testing purposes
   //Will delete once we figure out tf how to encrypt the images :(
	public static void main(String[] args) throws IOException {

        RSA test = new RSA(3);

		//Testing string encryption/decryption
		//Uses encrypt and decrypt methods
		//Note: I also tested encryption on a text file with the encrypt method and it worked just fine
		String plainText = "all the girls with heads inside a dream so now we live beside the pool where everything is good";
		BigInteger encrypt = encrypt(plainText);
		String decrypt = decrypt(encrypt);
		System.out.println("original message in plaintext: " + plainText);
		System.out.println("encrypted message: " + encrypt.toString());
		System.out.println("decrypted message in plaintext: "+ decrypt);


		System.out.println("encrypted message: " + encrypt);
		System.out.println("decrypted message in plaintext: "+ decrypt);
		
		/*
		//Third attempt for image encryption
		//Uses splitImage and encryptImage2 methods
		//Splitting original into smaller images, attempting to encrypt these smaller images
		BufferedImage[] originalToSmall = convertImage("happyduck.jpg");
		FileOutputStream encryptedImageFile2 = new FileOutputStream("encryptedhappyduck.jpg");

		for(int z = 0; z < originalToSmall.length;z++)
		{
			//Encrypting each individual image and then writing to encrypted file
			//This also doesn't work for some reason
			//I think there is problem with converting from bytes to big integer and back to bytes?
			byte[] smallerBytes = bufferToByte(originalToSmall[z], "jpg");
			encryptedImageFile2.write(encryptImage2(smallerBytes));
		}
		encryptedImageFile2.close();


		/*Second attempt for image encryption starts here
		//Uses getImageBytes and encryptImage methods
		byte[] originalBytes = getImageBytes("happyduck.jpg");
		String originalBytesToString = originalBytes.toString();
		System.out.println("bytes from original image: " + originalBytesToString);
		FileOutputStream encryptedImageFile = new FileOutputStream("encryptedhappyduck.jpg");
		encryptedImageFile.write(encryptImage(originalBytes));
		encryptedImageFile.close();
		*/

		/*First attempt for image encryption starts here*/
		//File i/o for the duck and the encrypted image
		
		/*First attempt for image encryption starts here
		//File i/o for the duck and the encrypted image*/
		FileInputStream originalImage =  new FileInputStream("happyduck.jpg");
		FileOutputStream encryptedImageFile = new FileOutputStream("encryptedhappyduck.jpg");
		byte[] buffer = new byte[256];


		//Testing whether or not the BigIntegers are equal to each other
		//TODO delete "test" after figuring wtf is going on with the encryption
		originalImage.read(buffer);
			BigInteger testOrig = new BigInteger(buffer);
			BigInteger testEnc = test.encryptBigInteger(testOrig);
			BigInteger testDec = test.decryptBigInteger(testEnc);

			System.out.println("Original BigInteger: " + testOrig.toString());
			System.out.println("Encrypted BigInteger: " + testEnc.toString());
			System.out.println("Decrypted BigInteger: " + testDec.toString());

			System.out.println(testOrig.equals(testDec));

			encryptedImageFile.write(test.encryptBigInteger(new BigInteger(buffer)).toByteArray());


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

		/*
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

	public static BigInteger getPrime(int length, Random r){
		return new BigInteger(length,100,r);
		}

	public static BigInteger calculatePhi(BigInteger numberP, BigInteger numberQ){
		return numberP.subtract(BigInteger.ONE).multiply(numberQ.subtract(BigInteger.ONE));
		}

	public static BigInteger calculateModulus(BigInteger numberP, BigInteger numberQ){
		return numberP.multiply(numberQ);
		}

	//Returns public key in the form (e,modulus)
	public static String getPublicKey(){
		return "Private Key Exponent: " + e.toString() +"\nModulus: "+ modulus.toString() + ")";
		}

	//Returns private key
	public static String getPrivateKey(){
		return privateKey.toString();
   }

	//Generates public key AND private key
	public static String generateKeyPair(){
		return "Public Key: " + getPublicKey() +"\n" +"Private Key: " + getPrivateKey();
	}

	//Encryption method for Strings
	public static BigInteger encrypt(String message){
		BigInteger messageBytes = new BigInteger(message.getBytes());
		return messageBytes.modPow(e,modulus);
	}

	public static BigInteger encryptBigInteger(BigInteger b) {
		return b.modPow(e, modulus);
	}
	//Second attempt at encrypting image
	public static byte[] encryptImage(byte[] image){
		byte [] encryptedImage = new byte[image.length];

		//encrypting every single byte of image
		//This actually takes alot of time...so i'm going to try to rethink this lol
		//Possible problem: could this be a problem of how i am trying to encrypt an 8 bit input using 1024 key?
		//Possible problem: when i print out the big integers, some are negative? from what i understand RSA cannot work on negative values right?
		for(int i = 0; i < image.length;i++){
			BigInteger byteToBigInteger = BigInteger.valueOf(image[i]);
			//System.out.println(byteToBigInteger.toString());
			encryptedImage[i] = (byteToBigInteger).modPow(e,modulus).byteValue();

		}
		return encryptedImage;
	}

	public static byte[] encryptImage2(byte[] image){
		BigInteger imageBigInt = new BigInteger(image);
		return (imageBigInt.modPow(e,modulus)).toByteArray();
	}




	//Decryption method for Strings
	public static String decrypt(BigInteger message){
		BigInteger decrypt = message.modPow(privateKey,modulus);
		return new String (decrypt.toByteArray());

	}

	public static BigInteger decryptBigInteger(BigInteger b) {
		return b.modPow(privateKey,modulus);
	}

	//Returns the bytes of image input
	public static byte[] getImageBytes(String path) throws IOException {
		BufferedImage original = ImageIO.read(new File (path));
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageIO.write(original, "jpg", output);

		return output.toByteArray();
	}

	//Converts buffered image to byte array
	public static byte[] bufferToByte(BufferedImage image, String format) throws IOException{
		 ByteArrayOutputStream input = new ByteArrayOutputStream();
	     ImageIO.write(image, format, input);
	     return input.toByteArray();
	}

	//This splits the original image into smaller images
	//Produces a buffer image array with all these small images
	public static BufferedImage[] convertImage(String path) throws IOException{
		File originalImage = new File(path);
		FileInputStream input = new FileInputStream(originalImage);
		BufferedImage image = ImageIO.read(input);

		//rows and column sizes can be changed
		int rows = 3;
		int cols = 3;
		int chunks = rows*cols;

		//width and height of the chunk of images
		int width = image.getWidth()/cols;
		int height = image.getHeight()/rows;

		int count = 0;

		//image array that will hold the chunks of images
		BufferedImage[] smallerImages = new BufferedImage[chunks];

		for(int x = 0; x < rows;x++){
			for(int y = 0; y < cols;y++){
				//creates image chunk with the	width and height that we established earlier
				smallerImages[count] = new BufferedImage(width,height,image.getType());
				//This should draw the new smaller images? I think?
				Graphics2D graphic = smallerImages[count].createGraphics();
				graphic.drawImage(image, 0, 0, width, height,width * y, height * x, width * y + width, height * x + height, null);
				graphic.dispose();
				count++;
				}
			}

		for(int i = 0; i<smallerImages.length;i++){
			//This actually creates these smaller images of type jpg
			ImageIO.write(smallerImages[i],"jpg", new File("img" + i + ".jpg"));
			}
		return smallerImages;
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					+ Character.digit(s.charAt(i+1), 16));
		}
		return data;
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
