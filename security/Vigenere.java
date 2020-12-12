package security;

import java.util.function.BiFunction;

public class Vigenere {
	
	//shifts message forward by characters in key
	public static String encrypt(String message, String key) {
		return shift(message, key, (original, shift) -> (char) ((original + shift - (2 * 'a')) % 26 + 'a'));
	}
	
	//shifts message backward by characters in key
	public static String decrypt(String message, String key) {
		return shift(message, key, (original, shift) -> (char) ((original - shift + 26) % 26 + 'a'));
	}

	//applies a function to characters in message and key to produce a shift
	public static String shift(String message, String key, BiFunction<Character, Character, Character> bf) {
		if (key.isEmpty()) return message;
		StringBuilder sb = new StringBuilder(message);
		key = key.toLowerCase();
		for (int i = 0, j = 0; i < message.length(); i++)
			if (Character.isLetter(sb.charAt(i)))
				sb.setCharAt(i, bf.apply(sb.charAt(i), key.charAt((j++) % key.length())));
		return sb.toString();
	}

}
