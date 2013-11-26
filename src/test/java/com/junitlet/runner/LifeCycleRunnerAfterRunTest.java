package com.junitlet.runner;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.junitlet.annotation.RunnerLifeCycleHandler;

@RunnerLifeCycleHandler(handler = MockupRunnerLifeCycleWorker.class, phase = RunnerLifeCycle.AFTER_RUN)
@RunWith(AssertionLifeCycleRunner.class)
public class LifeCycleRunnerAfterRunTest extends AbstractLifeCycleRunnerTest {
	@Test
	public void test() {
		MockupRunnerLifeCycleWorker.handle(getClass(), "test");
	}
}
