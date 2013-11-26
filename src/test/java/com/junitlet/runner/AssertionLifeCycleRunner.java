package com.junitlet.runner;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.runner.notification.RunNotifier;

import com.junitlet.runner.MockupRunnerLifeCycleWorker.Event;

public class AssertionLifeCycleRunner extends LifeCycleRunner {
	public AssertionLifeCycleRunner(Class<?> clazz) {
		super(clazz);
	}

	@Override
	public void run(RunNotifier notifier) {
		super.run(notifier);


		// now assert the results
		Class<?> clazz = LifeCycleRunnerAfterRunTest.class;
		Assert.assertEquals(
				Arrays.asList(new Event(clazz, "test"), new Event(clazz, RunnerLifeCycle.AFTER_RUN.name())),
				MockupRunnerLifeCycleWorker.getHandledEvents(clazz));
		
	}
	
}
