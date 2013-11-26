package com.junitlet.runner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.runner.Description;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.junitlet.annotation.ConfigurableTest;
import com.junitlet.annotation.TestConfiguration;
import com.junitlet.annotation.TestConfiguration.ConfigurationExtractor;
import com.junitlet.annotation.TestConfigurations;
import com.junitlet.annotation.TestConfigurer;

public class TestScenarioRunner extends BlockJUnit4ClassRunner {
//	private BlockJUnit4ClassRunner runner;
//
//	public TestScenarioRunner(BlockJUnit4ClassRunner runner) throws InitializationError {
//		super(runner.getTestClass().getJavaClass());
//		this.runner = runner;
//	}

	public TestScenarioRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}

	@Override
	protected List<FrameworkMethod> getChildren() {
		List<FrameworkMethod> tests = new ArrayList<FrameworkMethod>();
		tests.addAll(super.getChildren());
		tests.addAll(getConfiguredTests());
		
		return tests; 
	}
	
	private List<FrameworkMethod> getConfiguredTests() {
		return computeScenarioMethods();
	}

	@Override
	protected Description describeChild(FrameworkMethod method) {
		if (method instanceof ParametrizedFrameworkMethod) {
			return Description.createTestDescription(getTestClass().getJavaClass(),
					testName(method), ((ParametrizedFrameworkMethod)method).getConfiguration());
		}
		return super.describeChild(method);
	}
	
	@Override
	protected String testName(FrameworkMethod method) {
		String name = super.testName(method);
		if (method instanceof ParametrizedFrameworkMethod) {
			TestConfiguration configuration = ((ParametrizedFrameworkMethod)method).getConfiguration();
			String configurationName = ((ParametrizedFrameworkMethod)method).getConfiguratorName();
			if (configuration != null) {
				String confName = TestConfiguration.ConfigurationExtractor.getName(configuration);
				if (confName != null) {
					name += "_" + confName;
				} else {
					name += "_" + Arrays.toString(ConfigurationExtractor.getArguments(method.getMethod(), configuration));
				}
			} else if (configurationName != null) {
				name += "_" + configurationName + "_" + Arrays.toString(((ParametrizedFrameworkMethod) method).getArguments());
			}
		}
		return name;
	}
	


	@Override
	protected Statement methodInvoker(FrameworkMethod method, Object test) {
		if (method instanceof ParametrizedFrameworkMethod) {
			return new InvokeMethodWithArguments((ParametrizedFrameworkMethod)method, test);
		}
		return super.methodInvoker(method, test);
	}
	
	@Override
	protected void validateInstanceMethods(List<Throwable> errors) {
		validatePublicVoidNoArgMethods(After.class, false, errors);
		validatePublicVoidNoArgMethods(Before.class, false, errors);
		validateTestMethods(errors);
		validateScenarioMethods(errors);

		if (computeTestMethods().size() == 0 && computeScenarioMethods().size() == 0) {
			errors.add(new Exception("No runnable methods"));
		}
	}
	
	private void validateScenarioMethods(List<Throwable> errors) {
		// TODO: implement validation
	}
	
	
	@Override
	protected Statement withPotentialTimeout(FrameworkMethod method,
			Object test, Statement next) {
		long timeout= getTimeout(method);
		return timeout > 0 ? new FailOnTimeout(next, timeout) : next;
	}

	
	private long getTimeout(FrameworkMethod method) {
		if (method instanceof ParametrizedFrameworkMethod) {
			TestConfiguration config = ((ParametrizedFrameworkMethod)method).getConfiguration();
			return config == null ? 0L : config.timeout();
		}
		
		Test test = method.getMethod().getAnnotation(Test.class);
		return test == null ? 0L : test.timeout();
	}
	
	
	
	private List<FrameworkMethod> computeScenarioMethods() {
		Class<?> clazz = getTestClass().getJavaClass();
		// find test configurers
		Map<String, Method> configurers = new HashMap<>();
		Map<String, Object[]> configurerResults = new HashMap<>();
		for (Method m : clazz.getDeclaredMethods()) {
			m.setAccessible(true);
			TestConfigurer conf = m.getAnnotation(TestConfigurer.class);
			if (conf == null) {
				continue;
			}
			String configurerName = conf.value();
			if("".equals(configurerName)) {
				configurerName = m.getName();
			}
			if (!Modifier.isStatic(m.getModifiers())) {
				throw new IllegalArgumentException("Configurer must be static");
			}
			
			configurers.put(configurerName, m);
			
			try {
				configurerResults.put(configurerName, (Object[])m.invoke(null));
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new IllegalStateException(e);
			}
		}
		
		
		
		
		
		List<FrameworkMethod> methods = new LinkedList<FrameworkMethod>();

		for (Method m : clazz.getDeclaredMethods()) {
			m.setAccessible(true);
			List<TestConfiguration> allConfs = new LinkedList<TestConfiguration>();
			TestConfiguration conf = m.getAnnotation(TestConfiguration.class);
			if (conf != null) {
				allConfs.add(conf);
			}
			
			
			TestConfigurations confs = m.getAnnotation(TestConfigurations.class);
			if (confs != null) {
				for (TestConfiguration c : confs.value()) {
					if (c != null) {
						allConfs.add(c);
					}
				}
			}
			
			
			for (TestConfiguration c : allConfs) {
				FrameworkMethod method = new ParametrizedFrameworkMethod(m, c);
				methods.add(method);
			}
			
			ConfigurableTest confTest = m.getAnnotation(ConfigurableTest.class);
			if (confTest != null) {
				String[] configuratorNames = confTest.value();
				for (String configuratorName : configuratorNames) {
					Object[] confValues = configurerResults.get(configuratorName);
					for (Object confValue : confValues) {
						final Object[] args;
						if (confValue != null && confValue.getClass().isArray()) {
							args = (Object[])confValue;
						} else {
							args = new Object[] {confValue};
						}
						
						FrameworkMethod method = new ParametrizedFrameworkMethod(m, configuratorName, args);
						methods.add(method);
					}
				}
			}
		}
		
		return methods;
	}
}
 