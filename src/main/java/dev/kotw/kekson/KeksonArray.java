package dev.kotw.kekson;

import java.util.ArrayList;
import java.util.List;

public class KeksonArray {
	private List<KeksonValue<?>> values = new ArrayList<>();
	
	public <T> void add(T value) {
		values.add(new KeksonValue<>(value));
	}
	
	public KeksonValue<?> get(int index) {
		return values.get(index);
	}
	
	public List<KeksonValue> get() {
		return new ArrayList<>(values);
	}
	
	public void remove(int index) {
		values.remove(index);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for(int i = 0; i < get().size(); i++) {
			Object o = get(i).getValue();
			if(o instanceof String) o = StringUtil.toString((String) o, '"');
			builder.append(i!=0?",":"").append(o.toString());
		}
		builder.append("]");
		return builder.toString();
	}
}