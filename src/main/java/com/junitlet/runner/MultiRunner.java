package com.junitlet.runner;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.JUnit4;
import org.junit.runners.model.InitializationError;

import com.junitlet.annotation.WrapRunner;

public class MultiRunner extends BlockJUnit4ClassRunner {
	private List<Runner> runners = new ArrayList<>();
	
	
	public MultiRunner(Class<?> klass) throws InitializationError {
		super(klass);
		construct(klass, getRunnerTypes(klass));
	}

	
	private void construct(Class<?> clazz, Class<? extends Runner>[] classes) {
		try {
			for (int i = classes.length - 2;  i >= 0;  i--) {
				runners.add(classes[i].getConstructor(Class.class).newInstance(clazz));
			}
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
	@SuppressWarnings("unchecked") // nothing to do: arrays are not generic
	private static Class<? extends Runner>[] getRunnerTypes(Class<?> testClass) {
		WrapRunner wrapRunner = testClass.getAnnotation(WrapRunner.class);
		if (wrapRunner == null || wrapRunner.value().length == 0) {
			return new Class[] {JUnit4.class}; // the default JUnit runner
		}
		return  wrapRunner.value();
	}
	
}
