package com.junitlet.runner;

import java.util.Arrays;

import junit.framework.Assert;

import com.junitlet.runner.MockupRunnerLifeCycleWorker.Event;

abstract class AbstractLifeCycleRunnerTest {
	protected void test(RunnerLifeCycle phase) {
		String testName = new Throwable().getStackTrace()[1].getMethodName();
		
		MockupRunnerLifeCycleWorker.handle(getClass(), testName);
		Assert.assertEquals(
				Arrays.asList(
						new Event(getClass(), phase.name()), new Event(getClass(), testName)), 
						MockupRunnerLifeCycleWorker.getHandledEvents(getClass()));
	}
}
