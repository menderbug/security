import java.util.function.BiFunction;

public class Vigenere {
	
	public static void main(String[] args) {
		System.out.println(Vigenere.decrypt("qmflivo", "eecs"));
	}
	
	public static String encrypt(String message, String key) {
		return shift(message, key, (original, shift) -> (char) ((original + shift - (2 * 'a')) % 26 + 'a'));
	}
	
	public static String decrypt(String message, String key) {
		return shift(message, key, (original, shift) -> (char) ((original - shift + 26) % 26 + 'a'));
	}

	
	public static String shift(String message, String key, BiFunction<Character, Character, Character> bf) {
		StringBuilder sb = new StringBuilder(message);
		key = key.toLowerCase();
		for (int i = 0; i < message.length(); i++)
			if (Character.isLetter(sb.charAt(i)))
				sb.setCharAt(i, bf.apply(sb.charAt(i), key.charAt(i % key.length())));		//TODO fix the fact that it increments on non letters
		return sb.toString();
	}

}
