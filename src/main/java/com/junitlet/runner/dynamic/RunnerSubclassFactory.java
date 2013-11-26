package com.junitlet.runner.dynamic;

import java.io.IOException;
import java.io.InputStream;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.junit.runners.ParentRunner;

import com.junitlet.runner.ChildRunner;

public class RunnerSubclassFactory {
	public static <T, R extends ParentRunner<T>> R createRunner(R runner) {
		try {
			@SuppressWarnings("unchecked")
			Class<R> newRunerClass = createRunnerClass((Class<R>)runner.getClass());
			Class<?> testClass = runner.getTestClass().getJavaClass();
			return newRunerClass.getConstructor(Class.class).newInstance(testClass);
		} catch (IOException | ReflectiveOperationException e) {
			throw new IllegalArgumentException("Cannot extend runner " + runner);
		}
	}

	
	public static <T, R extends ParentRunner<T>> Class<R> createRunnerClass(Class<R> clazz) throws IOException {
		InputStream in = ChildRunner.class.getResourceAsStream(ChildRunner.class.getName().replace('.', '/') + ".java");
		String code = null; //TODO: read code from stream
		
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		JavaSourceFromString javaCodeObject = new JavaSourceFromString(ChildRunner.class.getName(), code);
		
		StandardJavaFileManager fm = compiler.getStandardFileManager(null, null, null);
		fm.getJavaFileForInput(StandardLocation.CLASS_OUTPUT, "", JavaFileObject.Kind.SOURCE);
		
		return null;
	}

}
