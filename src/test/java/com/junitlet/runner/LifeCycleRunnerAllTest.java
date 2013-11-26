package com.junitlet.runner;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.junitlet.annotation.RunnerLifeCycleHandler;
import com.junitlet.runner.MockupRunnerLifeCycleWorker.Event;

@RunnerLifeCycleHandler(
		handler = MockupRunnerLifeCycleWorker.class, 
		phase = RunnerLifeCycle.ALL)
@RunWith(LifeCycleRunner.class)
public class LifeCycleRunnerAllTest {
	// after run is not validated here. 
	@Test
	public void test() {
		MockupRunnerLifeCycleWorker.handle(getClass(), "test");
		Assert.assertEquals(
				Arrays.asList(
						new Event(getClass(), RunnerLifeCycle.BEFORE_CONSTRUCTOR.name()),  
						new Event(getClass(), RunnerLifeCycle.AFTER_CONSTRUCTOR.name()), 
						new Event(getClass(), RunnerLifeCycle.BEFORE_RUN.name()),  
						new Event(getClass(), "test") 
				), 
				MockupRunnerLifeCycleWorker.getHandledEvents(getClass()));
	}
}
