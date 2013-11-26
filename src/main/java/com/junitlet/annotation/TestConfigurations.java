package com.junitlet.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME) 
@Target(METHOD)
@Inherited
@Documented
public @interface TestConfigurations {
	TestConfiguration[] value();
}
