package dev.kotw.kekson;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeksonValue<T> {
	private T value;

	public KeksonValue(T value) {
		this.value = value;
	}	
			
	public T getValue() {
		return value;
	}
	
	public <R> R cast(Class<R> clz) {
		return (R) value;
	}
	
	public <R> R construct(Class<R> clz) {
		try {
			R r = clz.newInstance();
			KeksonObject obj = cast(KeksonObject.class);
			for(Field f : clz.getDeclaredFields()) {
				f.setAccessible(true);
				if(f.getModifiers()<3) {
					KeksonValue<?> val = obj.get(f.getName());
					if(val!=null) {
						Class<?> fclz = f.getType();
						Object o;
						if(fclz == List.class) {
							o = constructList(val.cast(KeksonArray.class), (ParameterizedType)f.getGenericType());
						} else if(fclz == Map.class) {
							o = constructMap(val.cast(KeksonObject.class), (ParameterizedType)f.getGenericType());
						} else {
							o = val.castType(fclz);
						}
						f.set(r, o);
					}
				}
			}
			return r;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
		
	public List<Object> constructList(KeksonArray arr, ParameterizedType t) {
		List<Object> ret = new ArrayList<>();
		for(KeksonValue<?> v : arr.get()) {
			Type n = t.getActualTypeArguments()[0];
			if(n instanceof Class) {
				ret.add(v.castType((Class<?>)n));
			} else {
				Class rawType = (Class<?>)((ParameterizedType)n).getRawType();
				if(rawType == List.class) {
					ret.add(constructList(v.cast(KeksonArray.class), (ParameterizedType)n));
				} else if(rawType == Map.class) {
					ret.add(constructMap(v.cast(KeksonObject.class), (ParameterizedType)n));
				}
			}
		}
		return ret;
	}
	
	public Map<String, Object> constructMap(KeksonObject obj, ParameterizedType t) {
		Map<String, Object> ret = new HashMap<>();
		for(String str : obj.keys()) {
			Type n = t.getActualTypeArguments()[1];
			if(n instanceof Class) {
				ret.put(str, obj.get(str).castType((Class<?>)n));
			} else {
				Class rawType = (Class<?>)((ParameterizedType)n).getRawType();
				if(rawType == Map.class) {
					ret.put(str, constructMap(obj.getO(str), (ParameterizedType)n));
				} else if(rawType == List.class) {
					ret.put(str, constructList(obj.get(str).cast(KeksonArray.class), (ParameterizedType)n));
				}
			}
		}
		return ret;
	}
	
	private Object castType(Class t) {
		Object o = getValue();
		if(t == int.class || t == Integer.class) {
			o = (int)(double) o;
		} else if(t == float.class || t == Float.class) {
			o = (float)(double) o;
		} else if(t == long.class || t == Long.class) {
			o = (long)(double) o;
		} else if(t == short.class || t == Short.class) {
			o = (short)(double) o;
		} else if(t == byte.class || t == Byte.class) {
			o = (byte)(double) o;
		} else if(t == double.class || t == Double.class) {
			o = (double) o;
		} else if(t == String.class) {
			o = (String) o;
		} else {
			o = construct(t);
		}
		return o;
	}
}