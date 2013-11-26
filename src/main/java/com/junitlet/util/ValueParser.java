package com.junitlet.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ValueParser {
	private static Map<Class<?>, Method> factoryMethods = new HashMap<Class<?>, Method>();
	
	static {
		try {
			factoryMethods.put(boolean.class, Boolean.class.getMethod("parseBoolean", String.class));
			factoryMethods.put(byte.class, Byte.class.getMethod("parseByte", String.class));
			factoryMethods.put(short.class, Short.class.getMethod("parseShort", String.class));
			factoryMethods.put(int.class, Integer.class.getMethod("parseInt", String.class));
			factoryMethods.put(long.class, Long.class.getMethod("parseLong", String.class));
			factoryMethods.put(float.class, Float.class.getMethod("parseFloat", String.class));
			factoryMethods.put(float.class, Double.class.getMethod("parseDouble", String.class));
			// Character ?
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalStateException(e);
		}
	}
	
	
	
	public <T> T parse(Class<T> clazz, String str) {
		try {
			Method factoryMethod = factoryMethods.get(clazz);
			if (factoryMethod != null) {
				@SuppressWarnings("unchecked")
				T result =  (T) factoryMethod.invoke(null, str);
				return result;
			}

			try {
				return clazz.getConstructor(String.class).newInstance(str);
			} catch (NoSuchMethodException e) {
				return clazz.getConstructor(CharSequence.class).newInstance(str);
			}
		} catch (IllegalArgumentException | IllegalAccessException
				| InvocationTargetException | InstantiationException | NoSuchMethodException e) {
			throw new IllegalArgumentException(e);
		}
	}

	
	public Object[] parse(Class<?>[] clazzes, String[] strings) {
		if (clazzes.length != strings.length) {
			throw new IllegalArgumentException("Number of types must be equal to number of values");
		}
		
		int n = clazzes.length;
		Object[] result = new Object[n];
		
		for (int i = 0;  i < n;  i++) {
			result[i] = parse(clazzes[i], strings[i]);
		}
		
		return result;
	}


	
	@SuppressWarnings("unchecked")
	public <T> T cast(Class<T> type, Object value) {
		if (value == null) {
			return null;
		}
		
		Class<?> valueType = value.getClass();
		
		if (type.isAssignableFrom(valueType)) {
			return (T)value;
		}
		
		if (Long.class.equals(valueType)) {
			if(Long.class.equals(type) || long.class.equals(type)) {
				return (T)value;
			}
			if(Integer.class.equals(type) || int.class.equals(type)) {
				return (T)Integer.valueOf(((Long)value).intValue());
			}
			if(Short.class.equals(type) || short.class.equals(type)) {
				return (T)Short.valueOf(((Long)value).shortValue());
			}
			if(Byte.class.equals(type) || byte.class.equals(type)) {
				return (T)Byte.valueOf(((Byte)value).byteValue());
			}
		}

		if (Double.class.equals(valueType)) {
			if(Double.class.equals(type) || double.class.equals(type)) {
				return (T)value;
			}
			if(Float.class.equals(type) || float.class.equals(type)) {
				return (T)Float.valueOf(((Double)value).floatValue());
			}
		}

		if(Boolean.class.equals(type) || boolean.class.equals(type)) {
			return (T)value;
		}
		
		return null;
	}
}
	
	
	
//	public Function getArguments(Class[] paramTypes, Object[] args) {
//		if (configuration == null) {
//			return null;
//		}
//		Argument[] arguments = configuration.arguments();
//		if (arguments == null) {
//			return null;
//		}
//		
//		int n = arguments.length;
//		
//		Class<?>[] paramTypes = method.getParameterTypes();
//		int nMethodArgs = paramTypes.length;
//		if(n != nMethodArgs) {
//			throw new IllegalArgumentException("Number of arguments is not equal to number of method parameters");
//		}
//		
//		Object[] values = new Object[n];
//		
//		for (int i = 0;  i < n;  i++) {
//			values[i] = Argument.ValueExtractor.getValue(paramTypes[i], arguments[i]);
//		}
//		
//		return values;
//	}
	
	

