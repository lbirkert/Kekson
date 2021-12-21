package dev.kotw.kekson;

public class KeksonParseException extends Exception {
	public KeksonParseException(String message, KeksonParser parser) {
		super(message + "\nAT: ->" + parser.kekson.substring(parser.index, Math.min(parser.index + 10, parser.kekson.length())));
	}
}