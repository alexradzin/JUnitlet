package com.junitlet.runner;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.junitlet.annotation.RunnerLifeCycleHandler;
import com.junitlet.annotation.RunnerLifeCycleHandlers;
import com.junitlet.runner.MockupRunnerLifeCycleWorker.Event;


@RunnerLifeCycleHandlers({
	@RunnerLifeCycleHandler(
			handler = MockupRunnerLifeCycleWorker.class, 
			phase = {RunnerLifeCycle.BEFORE_CONSTRUCTOR, RunnerLifeCycle.AFTER_CONSTRUCTOR}),
	@RunnerLifeCycleHandler(
			handler = MockupRunnerLifeCycleWorker.class, 
			phase = {RunnerLifeCycle.BEFORE_RUN})
})
@RunWith(LifeCycleRunner.class)
public class LifeCycleRunnerBeforeAndAfterConstructor2Test {
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
