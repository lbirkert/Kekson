package dev.kotw.kekson;

public class StringUtil {
	public static String toString(String str, char strChar) {
		StringBuilder builder = new StringBuilder();
		builder.append(strChar);
		for(int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if(c==strChar) {
				builder.append('\\').append(strChar);
			} else if(c=='\\') {
				builder.append("\\\\");
			} else builder.append(c);
		}
		builder.append(strChar);
		return builder.toString();
	}
}