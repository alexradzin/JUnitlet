package com.junitlet.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;


@Retention(RUNTIME) 
@Target(METHOD)
@Inherited
@Documented
public @interface TestConfiguration {
	String name() default DEFAULT_NAME;
	Argument[] arguments();
	Class<? extends Throwable> expected() default None.class;
	long timeout() default 0L; 
	String[] args() default {};
	
	/**
	 * Default empty exception
	 */
	static class None extends Throwable {
		private static final long serialVersionUID= 1L;		
		private None() {
		}
	}
	
	
	static final String DEFAULT_NAME = "##DEFAULT_NAME##";
	
	public static class ConfigurationExtractor {
		public static Object[] getArguments(Method method, TestConfiguration configuration) {
			if (configuration == null) {
				return null;
			}
			Argument[] arguments = configuration.arguments();
			if (arguments == null) {
				return null;
			}
			
			int n = arguments.length;
			
			Class<?>[] paramTypes = method.getParameterTypes();
			int nMethodArgs = paramTypes.length;
			if(n != nMethodArgs) {
				throw new IllegalArgumentException("Number of arguments is not equal to number of method parameters");
			}
			
			Object[] values = new Object[n];
			
			for (int i = 0;  i < n;  i++) {
				values[i] = Argument.ValueExtractor.getValue(paramTypes[i], arguments[i]);
			}
			
			return values;
		}
		
		public static String getName(TestConfiguration configuration) {
			String name = configuration.name();
			return TestConfiguration.DEFAULT_NAME.equals(name) ? null : name;  
		}
		
		public static Class<? extends Throwable> getExpected(TestConfiguration configuration) {
			return configuration == null || None.class.equals(configuration.expected()) ? null : configuration.expected();
		}
	}
}
