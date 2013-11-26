package com.junitlet.runner;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.junitlet.annotation.RunnerLifeCycleHandler;

@RunnerLifeCycleHandler(handler = MockupRunnerLifeCycleWorker.class, phase = RunnerLifeCycle.BEFORE_RUN)
@RunWith(LifeCycleRunner.class)
public class LifeCycleRunnerBeforeRunTest extends AbstractLifeCycleRunnerTest {
	@Test
	public void test() {
		test(RunnerLifeCycle.BEFORE_RUN);
	}
}
