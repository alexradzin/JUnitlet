package com.junitlet.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

class InstanceFactoryMethod<T> extends InstanceCreator<Method, T> {
	private Class<?> factory;
	private String factoryMethod;

	InstanceFactoryMethod(Class<T> type, Class<?> factory, String factoryMethod, boolean typedArgs, Object[] args) {
		super(type, typedArgs, args);
		this.factory = factory;
		this.factoryMethod = factoryMethod;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected MethodFunction[] getCreators() {
		Class<?> factoryClass = factory != null ? factory : getType();
		Method[] methods = factoryClass.getMethods();
		List<MethodFunction> functions = new ArrayList<MethodFunction>();
		for (Method method : methods) {
			if (Modifier.isStatic(method.getModifiers()) && factoryMethod.equals(method.getName())) {
				functions.add(new MethodFunction(method));
			}
		}
		return functions.toArray(new MethodFunction[functions.size()]);
	}

	@Override
	protected int getReturnValueRating(Function<?> creator) {
		Class<?> returnType = creator.getReturnType();
		if (getType().equals(returnType)) {
			return Rating.EXACT_MATCH.rating();
		}
		if (returnType.isAssignableFrom(getType())) {
			return Rating.ASSIGNABLE.rating();
		}
		
		return Rating.WRONG.rating();
	}

}
