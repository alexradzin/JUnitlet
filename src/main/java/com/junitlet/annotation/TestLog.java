package com.junitlet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.junitlet.runner.RunnerLifeCycle;
import com.junitlet.runner.TestLogger;

@RunnerLifeCycleHandler(handler=TestLogger.class, phase=RunnerLifeCycle.ALL)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface TestLog {
	public final static String TEST_CASE = "testCase";
	public final static String TEST_NAME = "testName";
	public final static String PHASE = "phase";
	
	Class<?> factory();
	String factoryMethod() default "getLogger";
	String log() default "debug";
	RunnerLifeCycle[] phase();
	String logMessage() default "${testCase}.${testName}: ${phase}";
	
	
}

