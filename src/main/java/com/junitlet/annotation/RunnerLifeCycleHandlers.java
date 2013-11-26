package com.junitlet.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME) 
@Target({TYPE, ANNOTATION_TYPE})
@Inherited
@Documented
public @interface RunnerLifeCycleHandlers {
	RunnerLifeCycleHandler[] value();
}
