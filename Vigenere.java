
public class Vigenere {
	
	public static void main(String[] args) {
		System.out.println(Vigenere.encrypt("midterm", "eecs"));
	}
	
	public static String encrypt(String message, String key) {
		StringBuilder sb = new StringBuilder();
		key = key.toLowerCase();
		for (int i = 0; i < message.length(); i++)
			sb.append(shift(message.charAt(i), key.charAt(i % key.length())));
		return sb.toString();
	}
	
	public static String decypt(String message, String key) {
		StringBuilder sb = new StringBuilder();
		key = key.toLowerCase();
		for (int i = 0; i < message.length(); i++)
			sb.append(shift(message.charAt(i), key.charAt(i % key.length())));	//TODO this doesnt work
		return sb.toString();
	}
	
	private static char shift(char original, char shift) {
		return (char) ((original + shift - (2 * 'a')) % 26 + 'a');
	}
	
}
