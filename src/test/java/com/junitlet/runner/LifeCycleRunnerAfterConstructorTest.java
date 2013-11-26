package com.junitlet.runner;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.junitlet.annotation.RunnerLifeCycleHandler;

@RunnerLifeCycleHandler(handler = MockupRunnerLifeCycleWorker.class, phase = RunnerLifeCycle.AFTER_CONSTRUCTOR)
@RunWith(LifeCycleRunner.class)
public class LifeCycleRunnerAfterConstructorTest extends AbstractLifeCycleRunnerTest {
	@Test
	public void test() {
		test(RunnerLifeCycle.AFTER_CONSTRUCTOR);
	}
}
