package com.junitlet.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.runner.Runner;
import org.junit.runners.JUnit4;

@Retention(RUNTIME) 
@Target(TYPE)
@Inherited
@Documented
public @interface WrapRunner {
	Class<? extends Runner>[] value() default JUnit4.class;
}
