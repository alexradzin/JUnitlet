package com.junitlet.runner;

import java.lang.reflect.Field;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.JUnit4;
import org.junit.runners.model.TestClass;

import com.junitlet.annotation.WrapRunner;

public class RunnerWrapperRunner extends Runner {
	private Runner runner;
	private Class<?> clazz;
	
	protected RunnerWrapperRunner(Runner runner) {
		this.runner = runner;
		clazz = findTestCaseClass(runner);
		beforeConstruction(clazz);
	}
	
	private Class<?> findTestCaseClass(Runner runner) {
		for (Class<?> runnerClass = runner.getClass(); !Object.class.equals(runnerClass); runnerClass = runnerClass.getSuperclass()) {
			final Field testCaseClassField; 
			try {
				testCaseClassField = runnerClass.getDeclaredField("fTestClass");
				testCaseClassField.setAccessible(true);
			} catch (NoSuchFieldException e) {
				// ignore and try super class.
				continue;
			}
			
			try {
				Object testCaseClass = testCaseClassField.get(runner);
				if (testCaseClass instanceof Class) {
					return (Class<?>)testCaseClass;
				} else if (testCaseClass instanceof TestClass) {
					return ((TestClass)testCaseClass).getJavaClass();
				}
			} catch(ReflectiveOperationException e) {
				throw new IllegalStateException(e);
			}
		}
		throw new IllegalStateException("Cannot discover test case class");
	}

	private void construct(Class<?> clazz, Class<? extends Runner>[] classes) {
		try {
			this.runner = classes[classes.length - 1].getConstructor(Class.class).newInstance(clazz);
			//Runner wrappedRunner = runner;
			for (int i = classes.length - 2;  i >= 0;  i--) {
				runner = classes[i].getConstructor(Runner.class).newInstance(runner);
			}
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public RunnerWrapperRunner(Class<?> clazz) {
		super();
		this.clazz = clazz;
		beforeConstruction(clazz);
		construct(clazz, getRunnerTypes(clazz));
	}
	

	@Override
	public Description getDescription() {
		return runner.getDescription();
	}

	@Override
	public void run(RunNotifier notifier) {
		runner.run(notifier);
	}

	
	@SuppressWarnings("unchecked") // nothing to do: arrays are not generic
	private static Class<? extends Runner>[] getRunnerTypes(Class<?> testClass) {
		WrapRunner wrapRunner = testClass.getAnnotation(WrapRunner.class);
		if (wrapRunner == null || wrapRunner.value().length == 0) {
			return new Class[] {JUnit4.class}; // the default JUnit runner
		}
		return  wrapRunner.value();
	}
	
	protected void beforeConstruction(Class<?> clazz) {
		// default empty implementation
	}
}
