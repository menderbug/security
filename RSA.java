import java.math.BigInteger;
import java.util.Random;

public class RSA {

	private BigInteger p;
	private BigInteger q;
	private BigInteger n;
	private BigInteger phi;
	private BigInteger e;
	private BigInteger d; // private key
	
	
	private int keyLength = 1024; 
	private Random random;
	private static BigInteger one = BigInteger.ONE;
	
   public RSA() {
		
		random = new Random();
		p = BigInteger.probablePrime(keyLength,random); 
		q = BigInteger.probablePrime(keyLength, random);
		n = p.multiply(q); 
		phi = p.subtract(one).multiply(q.subtract(one)); //phi(n) = (p–1)(q–1)
		
		e = new BigInteger ("65537"); // TO DO: e < n such that e is relatively prime to phi(n)
		
		//ed mod phi(n) = 1
		d = e.modInverse(phi); 
  	}	
	
	public static void main(String[] args) {
		
		RSA test = new RSA();
		
		String plainText = "HELLO";
		byte[] bytes = plainText.getBytes();
		BigInteger message = new BigInteger(bytes);
		
		BigInteger encrypt = test.encrypt(message);
		BigInteger decrypt = test.decrypt(encrypt);
		
		System.out.println("message in plaintext: " + new String(message.toByteArray()));
		System.out.println("encrypted message: " + encrypt);
		System.out.println("decrypted message in plaintext: "+ new String(decrypt.toByteArray()));
	}

	
	public BigInteger encrypt(BigInteger message) {
		return  message.modPow(e,n); // (e,n) is the public key
		
	}
	
	public BigInteger decrypt(BigInteger message) {
		return message.modPow(d,n);
		
	}

}
