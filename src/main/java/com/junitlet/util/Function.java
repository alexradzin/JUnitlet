package com.junitlet.util;

import java.lang.reflect.AccessibleObject;

public abstract class Function<F extends AccessibleObject> {
	private F function;
	
	protected Function(F function) {
		this.function = function;
	}
	
	
	protected abstract Class<?>[] getParameterTypes();
	protected abstract Class<?> getReturnType();
	protected abstract String getName();
	
	public abstract <R> R call(Object... args);    
    
    protected F getFunction() {
    	return function;
    }
}
