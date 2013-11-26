package com.junitlet.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodFunction extends Function<Method> {

	public MethodFunction(Method function) {
		super(function);
	}
	
    @Override
	public Class<?>[] getParameterTypes() {
    	return getFunction().getParameterTypes();
    }
    
    @Override
    public Class<?> getReturnType() {
    	return getFunction().getReturnType();
    }
    
    @Override
    public String getName() {
    	return getFunction().getName();
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public <R> R call(Object... args) {
		try {
			return (R) getFunction().invoke(null, args);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new IllegalArgumentException(e);
		}
	}
    
}
