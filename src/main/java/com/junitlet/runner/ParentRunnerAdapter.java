package com.junitlet.runner;

import java.util.Collections;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

public abstract class ParentRunnerAdapter<T> extends ParentRunner<T> {

	protected ParentRunnerAdapter(Class<?> testClass)
			throws InitializationError {
		super(testClass);
	}

	@Override
	protected Description describeChild(T child) {
		return null;
	}

	@Override
	protected List<T> getChildren() {
		return Collections.emptyList();
	}

	@Override
	protected void runChild(T arg0, RunNotifier arg1) {
		// stub
	}


}
