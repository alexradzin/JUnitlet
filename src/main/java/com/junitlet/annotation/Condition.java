package com.junitlet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.Callable;

import com.junitlet.filter.TrueFilter;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Inherited
@Documented
public @interface Condition {
	Class<? extends Callable<Boolean>> filter() default TrueFilter.class;
	Argument[] arguments() default {};
	boolean not() default false;
}
