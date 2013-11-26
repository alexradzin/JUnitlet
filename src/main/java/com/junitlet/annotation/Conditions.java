package com.junitlet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Inherited
@Documented
public @interface Conditions {
	Condition[] value();
	Relation relation() default Relation.OR; 
	
	
	public static enum Relation {
		AND, OR, XOR, ;
	}
}
