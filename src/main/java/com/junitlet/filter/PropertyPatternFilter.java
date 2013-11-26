package com.junitlet.filter;

import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public class PropertyPatternFilter implements Callable<Boolean> {
	public static enum Type {
		env {
			@Override
			public String getValue(String name) {
				return System.getenv(name);
			}
		},
		system {
			@Override
			public String getValue(String name) {
				return System.getProperty(name);
			}
		},
		;
		
		public abstract String getValue(String name);
	}
	
	private Type type = Type.system;
	private String propertyName;
	private Pattern pattern;
	

	public PropertyPatternFilter(String propertyName, Pattern pattern) {
		this.propertyName = propertyName;
		this.pattern = pattern;
	}
	
	public PropertyPatternFilter(String propertyName, String regex) {
		this(propertyName, Pattern.compile(regex));
	}
	
	
	@Override
	public Boolean call() throws Exception {
		String propertyValue = type.getValue(propertyName);
		return propertyValue != null && pattern.matcher(propertyValue).find();
	}

}
