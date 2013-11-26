package com.junitlet.runner;

import org.junit.runners.model.Statement;

import com.junitlet.annotation.TestConfiguration;

public class InvokeMethodWithArguments extends Statement {
	private final ParametrizedFrameworkMethod testMethod;
	private final Object target;

	public InvokeMethodWithArguments(ParametrizedFrameworkMethod testMethod, Object target) {
		this.testMethod = testMethod;
		this.target = target;
	}

	@Override
	public void evaluate() throws Throwable {
		try {
			testMethod.invokeExplosively(target, testMethod.getArguments(target));
		} catch (Throwable e) {
			Class<? extends Throwable> expectedException = 
					TestConfiguration.ConfigurationExtractor.getExpected(testMethod.getConfiguration());
			if (expectedException == null) {
				throw e;
			}
			if (!expectedException.isAssignableFrom(e.getClass())) {
				throw new AssertionError("Expected test to throw " + expectedException);
			}
		}
	}
}
