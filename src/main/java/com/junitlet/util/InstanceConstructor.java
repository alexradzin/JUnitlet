package com.junitlet.util;

import java.lang.reflect.Constructor;

public class InstanceConstructor<T> extends InstanceCreator<Constructor<T>, T> {

	protected InstanceConstructor(Class<T> type, boolean typedArgs, Object[] args) {
		super(type, typedArgs, args);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected ConstructorFunction[] getCreators() {
		Constructor<T>[] constructors = (Constructor<T>[])getType().getConstructors();
		int n = constructors.length;
		ConstructorFunction[] functions = new ConstructorFunction[n];
		for (int i = 0;  i < n;  i++) {
			functions[i] = new ConstructorFunction(constructors[i]);
		}
		
		return functions;
	}

	@Override
	protected int getReturnValueRating(Function<?> creator) {
		return Rating.EXACT_MATCH.rating();
	}

}
