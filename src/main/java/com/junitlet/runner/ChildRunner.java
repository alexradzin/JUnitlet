package com.junitlet.runner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import com.junitlet.annotation.CopySourceToTarget;

@CopySourceToTarget
public class ChildRunner extends ParentRunnerAdapter<FrameworkMethod> {
	private Map<RunnerLifeCycle, RunnerLifeCycleWorker> runnerHandlers;
	private Map<RunnerLifeCycle, RunnerLifeCycleWorker> testCaseHandlers;
	private Class<?> testClass;

	private ParentRunnerAdapter<FrameworkMethod> childDescriber;
	private Collection<ParentRunnerAdapter<FrameworkMethod>> childrenFetchersBefore;
	private Collection<ParentRunnerAdapter<FrameworkMethod>> childrenFetchersAfter;
	
	protected ChildRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
		//TODO get childDesciber, childrenFetchersBefore, childrenFetchersAfter from annotation
	}

	@Override
	protected Description describeChild(FrameworkMethod frameworkMethod) {
		Description description = null;
		if (childDescriber != null) {
			description = childDescriber.describeChild(frameworkMethod);
		}
		if (description == null) {
			description = super.describeChild(frameworkMethod);
		}
		
		return description;
	}

	@Override
	protected List<FrameworkMethod> getChildren() {
		List<FrameworkMethod> children = new ArrayList<FrameworkMethod>();
		children.addAll(fetchChildren(childrenFetchersBefore));
		children.addAll(super.getChildren());
		children.addAll(fetchChildren(childrenFetchersAfter));
		return children;
	}

	@Override
	protected void runChild(FrameworkMethod frameworkMethod, RunNotifier notifier) {
		handle(RunnerLifeCycle.BEFORE_TEST_RUN);
		super.runChild(frameworkMethod, notifier);
		handle(RunnerLifeCycle.AFTER_TEST_RUN);
	}
	
	
	private void handle(RunnerLifeCycle phase) {
		RunnerLifeCycleWorker handler = testCaseHandlers.get(phase);
		if (handler == null) {
			handler = runnerHandlers.get(phase);
		}
				
		handle(testClass, phase, handler);
	}
	
	protected  void handle(Class<?> clazz, RunnerLifeCycle phase, RunnerLifeCycleWorker handler) {
		if (handler != null) {
			try {
				handler.handle(clazz, phase);
			} catch (Exception e) {
				e.printStackTrace();
				throw new AssertionError("Execution of runner lifecycle handler failed on phase " + phase + ":" + e.getMessage());
			}
		}
	}

	
	private Collection<FrameworkMethod> fetchChildren(Collection<ParentRunnerAdapter<FrameworkMethod>> childrenFetchers) {
		Collection<FrameworkMethod> children = new ArrayList<>();
		if (childrenFetchers != null) {
			for (ParentRunnerAdapter<FrameworkMethod> childrenFetcher : childrenFetchers) {
				children.addAll(childrenFetcher.getChildren());
			}
		}
		return children;
	}
}
