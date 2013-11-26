package com.junitlet.runner;

import java.lang.reflect.Method;
import java.util.Objects;

import org.junit.runners.model.FrameworkMethod;

import com.junitlet.annotation.TestConfiguration;

public class ParametrizedFrameworkMethod extends FrameworkMethod {
	private Object[] args;
	private TestConfiguration configuration;
	private Method configurator;
	private String configuratorName;
	
	public ParametrizedFrameworkMethod(Method method, TestConfiguration configuration) {
		this(method, null, TestConfiguration.ConfigurationExtractor.getArguments(method, configuration));
		this.configuration = configuration;
	}

	public ParametrizedFrameworkMethod(Method method, String configuratorName, Object[] args) {
		super(method);
		this.configuratorName = configuratorName;
		this.args = args;
	}
	
	public ParametrizedFrameworkMethod(Method method, Method configurator) {
		super(method);
		this.configurator = Objects.requireNonNull(configurator);
	}
	
	public Object[] getArguments(Object target) {
		if (configurator != null) {
			try {
				return (Object[]) configurator.invoke(target);
			} catch (ReflectiveOperationException e) {
				throw new IllegalStateException(e);
			}
		}
		return args;
	}
	
	public TestConfiguration getConfiguration() {
		return configuration;
	}
	
	public String getConfiguratorName() {
		return configuratorName;
	}
	
	public Object[] getArguments() {
		return args;
	}
}
