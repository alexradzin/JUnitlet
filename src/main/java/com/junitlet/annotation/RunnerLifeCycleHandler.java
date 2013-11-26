package com.junitlet.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.junitlet.runner.RunnerLifeCycle;
import com.junitlet.runner.RunnerLifeCycleWorker;

@Retention(RUNTIME) 
@Target({TYPE, ANNOTATION_TYPE})
@Inherited
@Documented
public @interface RunnerLifeCycleHandler {
	RunnerLifeCycle[] phase();
	Class<? extends RunnerLifeCycleWorker> handler();
}
