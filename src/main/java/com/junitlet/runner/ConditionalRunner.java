package com.junitlet.runner;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import com.junitlet.filter.FilterFactory;

public class ConditionalRunner extends BlockJUnit4ClassRunner {
	public ConditionalRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

    @Override
	protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
		FilterFactory factory = new FilterFactory();
        Description description = describeChild(method);
		
		Method m = method.getMethod();
		Callable<Boolean> filter = factory.createFilter(m);
		if (filter != null && !filter(filter)) {
			notifier.fireTestIgnored(description);
			return;
		}
	
		super.runChild(method, notifier);
    }
	
	
	private boolean filter(Callable<Boolean> filter) {
		try {
			return filter.call();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
}
