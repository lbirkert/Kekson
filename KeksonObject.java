import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KeksonObject {
	private Map<String, KeksonValue<?>> values = new HashMap<>();
	
	public <T> void put(String key, T value) {
		values.put(key, new KeksonValue<>(value));
	}
	
	public void remove(String key) {
		values.remove(key);
	}
	
	public KeksonValue<?> get(String key) {
		return values.get(key);
	}
	
	public KeksonObject getO(String key) {
		return get(key).cast(KeksonObject.class);
	}
	
	public Set<String> keys() {
		return values.keySet();
	}
	
	public Collection<KeksonValue<?>> values() {
		return values.values();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		
		String[] keys = keys().toArray(new String[keys().size()]);
		KeksonValue<?>[] values = values().toArray(new KeksonValue<?>[values().size()]);
		
		for(int i = 0; i < keys.length; i++) {
			Object o = values[i].getValue();
			if(o instanceof String) o = StringUtil.toString((String) o, '"');
			builder.append(i!=0?",":"").append(StringUtil.toString(keys[i], '"')).append(":").append(o.toString());
		}
		
		builder.append("}");
		return builder.toString();
	}
}