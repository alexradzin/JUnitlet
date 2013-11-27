package com.junitlet.runner.dynamic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.junit.runners.ParentRunner;

import com.junitlet.runner.ChildRunner;

public class RunnerSubclassFactory {
	public <T, R extends ParentRunner<T>> R createRunner(R runner) {
		try {
			@SuppressWarnings("unchecked")
			Class<R> newRunerClass = createRunnerClass((Class<R>)runner.getClass());
			Class<?> testClass = runner.getTestClass().getJavaClass();
			return newRunerClass.getConstructor(Class.class).newInstance(testClass);
		} catch (IOException | ReflectiveOperationException e) {
			throw new IllegalArgumentException("Cannot extend runner " + runner);
		}
	}

	
	public <T, R extends ParentRunner<T>> Class<R> createRunnerClass(Class<R> clazz) throws IOException {
		InputStream in = ChildRunner.class.getResourceAsStream(ChildRunner.class.getName().replace('.', '/') + ".java");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder buf = new StringBuilder();
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			// TODO: replace extends ParentRunnerAdapter<FrameworkMethod> by extends clazz.getName() 
			buf.append(line).append("\n");
		}
		
		String code = buf.toString(); 
		
		// compile the new runner.
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		JavaSourceFromString javaCodeObject = new JavaSourceFromString(ChildRunner.class.getName(), code);
		
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		fileManager.getJavaFileForInput(StandardLocation.CLASS_OUTPUT, "", JavaFileObject.Kind.SOURCE);
		
//		CompilationTask compilationTask = compiler.getTask(out, fileManager, diagnosticListener, options, classes, Collections.singleton(javaCodeObject));
//		compilationTask.call();
		
		return null;
	}

}
