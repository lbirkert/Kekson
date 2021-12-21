package dev.kotw.kekson;

import java.util.Set;
import java.util.Stack;

public class KeksonParser {
	int index = 0;
	
	StringBuilder builder = null;
	String string = null;
	String key = null;
	StringBuilder number;
	boolean stringEscape = false;
	char stringChar = ' ';
	
	Stack<KeksonValue<?>> queue = new Stack<>();
	
	String kekson = "";
	
	public KeksonParser(String kekson) {
		this.kekson = kekson;
	}
	
	public KeksonValue parse() throws KeksonParseException {
		try {
			for(index = 0; index < kekson.length(); index++) {
				if(builder != null) parseString(kekson.charAt(index));
				else parse(kekson.charAt(index));
			}
			return queue.pop();
		} catch(Exception e) {
			e.printStackTrace();
			throw new KeksonParseException(e.getMessage(), this);
		}
	}
	
	private void add(KeksonValue<?> val, boolean r) throws KeksonParseException {
		if(queue.size()!=0) {
			if(queue.lastElement().getValue() instanceof KeksonObject) {
				if(key==null) throw  new KeksonParseException("Key cannot be empty!", this);
				queue.lastElement().cast(KeksonObject.class).put(key, val.getValue());
				key = null;
			} else if(queue.lastElement().getValue() instanceof KeksonArray) {
				queue.lastElement().cast(KeksonArray.class).add(val.getValue());
			}
		}
		
		if(r) queue.push(val);
	}
	
	private void parse(char c) throws KeksonParseException {
		if(c=='[') {
			KeksonArray kek = new KeksonArray();
			KeksonValue<?> val = new KeksonValue<>(kek);
			
			add(val, true);
		} else if(c=='{') {
			KeksonObject kek = new KeksonObject(); 			KeksonValue<?> val = new KeksonValue<>(kek);
			
			add(val, true);	
		} else if(c=='"' || c=='\'') {
			builder = new StringBuilder();
			stringChar = c;
		} else if(c==':') {
			key = string;
			string = null;
			number = new StringBuilder();
		} else if(c=='}'||c==','||c==']') {
			if(string!=null) {
				if(key!=null) queue.lastElement().cast(KeksonObject.class).put(key, string);
				else queue.lastElement().cast(KeksonArray.class).add(string);
				string = null;
			} else if(number!=null&&!number.toString().equals("")) {
				try {
					double n = Double.parseDouble(number.toString());
					if(key!=null) queue.lastElement().cast(KeksonObject.class).put(key, n);
					else queue.lastElement().cast(KeksonArray.class).add(n);
					number = null;
				} catch(NumberFormatException e) {
					throw new KeksonParseException("Invalid Number Format", this);
				}
			}
			if(c==',' && queue.lastElement().getValue() instanceof KeksonArray) number = new StringBuilder();
			key = null;
			if((c=='}' || c==']')&& queue.size()>1) queue.pop();
		} else if(c!=' '&&number!=null) {
			number.append(c);
		}
	}
	
	private void parseString(char c) {
		if(stringEscape) {
			builder.append(c);
			stringEscape = false;
		} else if(c=='\\') {
			stringEscape = true;
		} else if(c==stringChar) {
			string = builder.toString();
			builder = null;
		} else {
			builder.append(c);
		}
	}
	
	public static <T> T parseAndConstruct(String kekson, Class<T> clz) {
		try {
			return (T) new KeksonParser(kekson).parse().construct(clz);
		} catch(KeksonParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}