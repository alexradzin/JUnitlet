package com.junitlet.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class ConstructorFunction extends Function<Constructor<?>> {
	protected ConstructorFunction(Constructor<?> function) {
		super(function);
	}

    @Override
	protected Class<?>[] getParameterTypes() {
    	return getFunction().getParameterTypes();
    }
    
    @Override
    public Class<?> getReturnType() {
    	return getFunction().getDeclaringClass();
    }
    
    @Override
    public String getName() {
    	return getFunction().getName();
    }
    
	@Override
	@SuppressWarnings("unchecked")
	public <R> R call(Object... args) {
		try {
			return (R) getFunction().newInstance(args);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new IllegalArgumentException(e);
		}
	}
    
}
