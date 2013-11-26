package com.junitlet.util;

import java.lang.reflect.AccessibleObject;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class InstanceCreator<F extends AccessibleObject, T> {
	private Class<T> type;
	private boolean typedArgs;
	private Object[] args;

	private ValueParser parser = new ValueParser();
	
	protected static enum Rating {
		WRONG(0),
		CASTED_ASSIGNABLE(10),
		ASSIGNABLE(100),
		EXACT_MATCH(1000),
		;
		private int rating;
		
		private Rating(int rating) {
			this.rating = rating;
		}
		
		int rating() {
			return rating;
		}
	}
	
	
	public static <T, C extends InstanceCreator<?, T>> InstanceCreator<?, T> createCreator(Class<T> type, Class<?> factory, String factoryMethod, boolean typedArgs, Object[] args) {
		return factoryMethod == null ? 
				new InstanceConstructor<T>(type, typedArgs, args) :
				new InstanceFactoryMethod<T>(type, factory, factoryMethod, typedArgs, args);
	}
	
	
	protected InstanceCreator(Class<T> type, boolean typedArgs, Object[] args) {
		this.type = type;
		this.typedArgs = typedArgs;
		this.args = args;
	}
	
    public Function<?> findCreator() {
    	Function<F>[] creators = getCreators();

    	int maxRating = 0;
    	Function<F> bestCreator = null;
    	for (Function<F> creator : creators) {
    		int rating = getRating(creator, typedArgs, args);
    		if (rating > maxRating) {
    			maxRating = rating;
    			bestCreator = creator;
    		}
    	}
    	
    	return bestCreator;
    }
	
    
    private int getRating(Function<F> creator, boolean typedArgs, Object[] args) {
    	int paramsRating = getParamsRating(creator.getParameterTypes(), typedArgs, args);
    	int returnValueRating = getReturnValueRating(creator);
    	if (paramsRating == 0 || returnValueRating == 0) {
    		return 0;
    	}
    	return paramsRating + returnValueRating;
    }


    public Class<T> getType() {
    	return type;
    }
    
	protected int getParamsRating(Class<?>[] paramTypes, boolean typedArgs, Object[] args) {
		int rating = 0;
		int p = 0;
		int a = 0;
		
		for (;  p < paramTypes.length && a < args.length;  p++, a++) {
			int r = getParamRating(paramTypes[p], args[a]);
			if (r == 0) {
				return 0;
			}
			rating += r;
		}
		
		if(args.length > paramTypes.length) {
			if (paramTypes.length == 0 || !paramTypes[paramTypes.length - 1].isArray()) {
				// number of arguments does not match number of parameters
				return Rating.WRONG.rating();
			}
			// The last parameter is array, so it can be ellipsis, so we will try to fill it with the rest of the arguments.
			for (;  a < args.length;  a++) {
				int r = getParamRating(paramTypes[p], args[a]);
				if (r == 0) {
					return 0;
				}
				rating += r;
			}
		}
		
		return rating;
	}

	private int getParamRating(Class<?> param, Object value) {
		Class<?> arg = value == null ? null : value.getClass();
		if (param.equals(arg)) {
			return Rating.EXACT_MATCH.rating();
		}
		try {
			if (isAssignable(param, arg, value)) {
				return Rating.ASSIGNABLE.rating();
			}
			if (!typedArgs && value != null && String.class.equals(arg)) {
					parser.parse(param, (String)value);
					return Rating.CASTED_ASSIGNABLE.rating(); 
			}
		} catch (IllegalArgumentException e) {
			// We failed to parse this argument. This means that there is no way
			// to adopt given arguments to method prototype being discovered.
			// So, we have to return 0 hear that means that this method will not be used.
			return 0;
		}
		// there is no match between parameter type and argument value
		return 0;
	}
	
    
    protected abstract <C extends Function<F>> C[] getCreators();
    protected abstract int getReturnValueRating(Function<?> creator);

    private final static Map<Class<?>, Collection<Class<?>>> assignability = new HashMap<>(); {
    	assignability.put(Long.class, Arrays.<Class<?>>asList(Long.class, Integer.class, Short.class, Byte.class, long.class, int.class, short.class, byte.class));
    	assignability.put(long.class, Arrays.<Class<?>>asList(Long.class, Integer.class, Short.class, Byte.class, long.class, int.class, short.class, byte.class));
    	assignability.put(Integer.class, Arrays.<Class<?>>asList(Integer.class, Short.class, Byte.class, int.class, short.class, byte.class));
    	assignability.put(int.class, Arrays.<Class<?>>asList(Integer.class, Short.class, Byte.class, int.class, short.class, byte.class));
    	assignability.put(Short.class, Arrays.<Class<?>>asList(Short.class, Byte.class, short.class, byte.class));
    	assignability.put(short.class, Arrays.<Class<?>>asList(Short.class, Byte.class, short.class, byte.class));
    	assignability.put(Byte.class, Arrays.<Class<?>>asList(Byte.class, byte.class));
    	assignability.put(byte.class, Arrays.<Class<?>>asList(Byte.class, byte.class));
    	assignability.put(Boolean.class, Arrays.<Class<?>>asList(Boolean.class, boolean.class));
    	assignability.put(boolean.class, Arrays.<Class<?>>asList(Boolean.class, boolean.class));
    }
    
    private boolean isAssignable(Class<?> left, Class<?> right, Object value) {
		if (left.isAssignableFrom(right)) {
			return true;
		}
		Collection<Class<?>> assignables = assignability.get(left);
		if (assignables != null && assignables.contains(right)) {
			return true;
		}
		
		if (Number.class.isAssignableFrom(left) && Number.class.isAssignableFrom(right)) {
			if (new ValueParser().cast(left, value) != null) {
				return true;
			}
		}
		
		if (!String.class.equals(left) && String.class.equals(right)) {
			return new ValueParser().parse(left, (String) value) != null;
		}
		return false;
    }
}
