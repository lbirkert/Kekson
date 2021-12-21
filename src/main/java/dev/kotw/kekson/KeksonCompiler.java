package dev.kotw.kekson;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class KeksonCompiler {
	public static String compile(KeksonValue<?> val) {
		if(val.getValue() instanceof KeksonArray) {
			return val.cast(KeksonArray.class).toString();
		} else if(val.getValue() instanceof KeksonObject) {
			return val.cast(KeksonObject.class).toString();
		}
		return null;
	}
	
	public static String deconstructAndCompile(Object o) {
		return compile(new KeksonValue<>(deconstruct(o)));
	}
	
	public static Object deconstruct(Object o) {
		Class<?> t = o.getClass();
		if(t == Integer.class) {
			o = (double)(int) o;
		} else if(t == Float.class) {
			o = (double)(float) o;
		} else if(t == Long.class) {
			o = (double)(long) o;
		} else if(t == Short.class) {
			o = (double)(short) o;
		} else if(t == Byte.class) {
			o = (double)(byte) o;
		} else if(t == Double.class) {
			o = (double) o;
		} else if(t == String.class) {
			o = (String) o;
		} else if(o instanceof Map) {
			o = deconstructMap((Map<String, Object>)o);
		} else if(o instanceof List) {
			o = deconstructList((List<Object>)o);
		} else {
			o = deconstructObject(o);
		}
		return o;
	}
	
	public static KeksonObject deconstructMap(Map<String, Object> m) {
		KeksonObject obj = new KeksonObject();
		for(String key : m.keySet()) {
			obj.put(key, deconstruct(m.get(key)));
		}
		return obj;
	}
	
	public static KeksonObject deconstructObject(Object o) {
		try {
			KeksonObject obj = new KeksonObject();
			for(Field f : o.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				if(f.getModifiers() < 3) obj.put(f.getName(), deconstruct(f.get(o)));
			}
			return obj;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static KeksonArray deconstructList(List<Object> l) {
		KeksonArray arr = new KeksonArray();
		for(Object o : l) {
			arr.add(deconstruct(o));
		}
		return arr;
	}
}