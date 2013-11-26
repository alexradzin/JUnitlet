package com.junitlet.runner;

public interface RunnerLifeCycleWorker {
	public void handle(Class<?> testClass, RunnerLifeCycle phase);
}
