package com.junitlet.annotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.junitlet.util.Function;
import com.junitlet.util.InstanceCreator;
import com.junitlet.util.ValueParser;


public @interface Argument {
	Class<?> type() default Default.class;
	String name() default DEFAULT;
	Class<?> factory() default Default.class;
	String factoryMethod() default DEFAULT;
	long integer() default DEFAULT_INT;
	String value() default DEFAULT;
	double number() default Double.NaN;
	String[] args() default {DEFAULT};
	
	public static class Default {/* default dummy class */}
	public static final String DEFAULT = "##DEFAULT##";
	static final int DEFAULT_INT = 1357837717; // just epoch that was when I wrote this. 

	
	
	public static class ValueExtractor {
		public static <T> T getValue(Class<T> type, Argument argument) {
			return new ValueParser().cast(type, getValue(argument));
		}
		
		public static String getFactoryMethod(Argument argument) {
			String factoryMethod = argument.factoryMethod();
			return DEFAULT.equals(factoryMethod) ?  null : factoryMethod;
		}
		
		public static Object[] getValues(Argument[] arguments) {
			int n = arguments.length;
			Object[] values = new Object[n];
			for (int i = 0;  i < n;  i++) {
				values[i] = getValue(arguments[i]);
			}
			return values;
		}
		
		
		public static Object  getValue(Argument argument) {
			try {
				return getValueImpl(argument);
			} catch (IllegalArgumentException | SecurityException e) {
				throw new IllegalStateException(e);
			}
		}
		

		private static Object getValueImpl(Argument argument) 
				throws IllegalArgumentException, SecurityException {
			
			if (argument == null) {
				return null;
			}
			
			Class<?> clazz = null;
			Class<?> factoryClass = null;
			String factoryMethod = null;
			
			// try to use factory
			if (!Default.class.equals(argument.factory())) {
				factoryClass = argument.factory();
				factoryMethod = getFactoryMethod(argument);
				if (factoryMethod == null) {
					throw new IllegalArgumentException("No factory method is specified for factory " + factoryClass);
				}
			}
			// try to instantiate class using constructor that accepts string or static factory method
			if (!Default.class.equals(argument.type())) {
				clazz = argument.type();
				factoryMethod = getFactoryMethod(argument);
			}
			
			Collection<Object> args = new ArrayList<Object>();
			Boolean typedArgs = findArguments(argument, args);
			Object[] argsArray;
			if (typedArgs == null) {
				// dirty trick: typedArgs==null means that args should be null  
				typedArgs = false;
				args = null;
				argsArray = null;
			} else {
				argsArray = args.toArray();
			}

			// if there is a way to create object via constructor, factory or factory method, do it now
			if (clazz != null || factoryClass != null || factoryMethod != null) {
				Function<?> creator =  InstanceCreator.createCreator(clazz, factoryClass, factoryMethod, typedArgs, argsArray).findCreator();
				if (creator != null) {
					return creator.call(argsArray);
				}
			}
			
			// no special object creation is needed. Just return args as-is
			if (args == null) {
				return null;
			}
			//TODO: how to deal with one element long arrays?
			return args.size() == 1 ? args.iterator().next() : args; 
		}
		
		
		private static Boolean findArguments(Argument argument, Collection<Object> args) {
			// try to take double
			if (!Double.isNaN(argument.number())) {
				args.add(argument.number());
				return true;
			}
			
			// try to take string
			if (!DEFAULT.equals(argument.value())) {
				args.add(argument.value());
				return true;
			}
			
			if (DEFAULT_INT != argument.integer()) {
				args.add(argument.integer());
				return true;
			}
			
			
			String[] argumentArgs = argument.args();
			if(argumentArgs.length == 1 && DEFAULT.equals(argumentArgs[0])) {
				return null;
			}
			
			args.addAll(Arrays.asList(argument.args()));
			return false;
		}
	}
	
}
