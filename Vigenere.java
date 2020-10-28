
public class Vigenere {
	
	public static String encrypt(String message, String key) {
		StringBuilder sb = new StringBuilder();
		key = key.toLowerCase();
		for (int i = 0; i < message.length(); i++)
			sb.append(message.charAt(i) + key.charAt(i % key.length()) - 'a');
		return sb.toString();
	}
	
}
