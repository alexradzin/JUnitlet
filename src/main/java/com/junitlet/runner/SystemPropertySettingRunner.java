package com.junitlet.runner;

import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;

import com.junitlet.annotation.RunnerLifeCycleHandler;

@RunnerLifeCycleHandler(handler=SystemPropertySetter.class, phase=RunnerLifeCycle.ALL)
public class SystemPropertySettingRunner extends LifeCycleRunner {
	// although InitializationError is not thrown here it can be thrown by subclass
	public SystemPropertySettingRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}
	
	public SystemPropertySettingRunner(Runner runner) {
		super(runner);
	}
	
}
