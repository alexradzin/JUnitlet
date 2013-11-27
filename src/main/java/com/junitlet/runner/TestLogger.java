package com.junitlet.runner;

import java.util.Arrays;

import com.junitlet.annotation.TestLog;

public class TestLogger implements RunnerLifeCycleWorker {
	private static enum LogMessageTag {
		testCase {
			String replacement(Class<?> testClass, String test, RunnerLifeCycle phase) {
				return testClass.getName();
			}
		},
		testName {
			String replacement(Class<?> testClass, String test, RunnerLifeCycle phase) {
				return test;
			}
		},
		phase {
			String replacement(Class<?> testClass, String test, RunnerLifeCycle phase) {
				return phase.name();
			}
		},
		;
		
		abstract String replacement(Class<?> testClass, String test, RunnerLifeCycle phase);
		
		static String createLogMessage(String template, Class<?> testClass, String test, RunnerLifeCycle phase) {
			String message = template;
			for (LogMessageTag tag : LogMessageTag.values()) {
				String tagName = tag.name();
				String target = "${" + tagName + "}";
				message = message.replace(target, tag.replacement(testClass, test, phase));
			}
			return message;
		}
	}
	@Override
	public void handle(Class<?> testClass, RunnerLifeCycle phase) {
		TestLog testLog = testClass.getAnnotation(TestLog.class);
		
		
		if (testLog == null) {
			return;
		}
		
		
		if (!Arrays.asList(testLog.phase()).contains(phase)) {
			return;
		}
		
		String logMessage = LogMessageTag.createLogMessage(testLog.logMessage(), testClass, "test", phase);

		try {
			testLog.factory().getMethod(testLog.factoryMethod()).invoke(logMessage);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException(e);
		}
	}

}
