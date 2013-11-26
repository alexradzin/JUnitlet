package com.junitlet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.junitlet.runner.RunnerLifeCycle;
import com.junitlet.runner.SetPropertyCondition;
import com.junitlet.runner.SystemPropertySetter;

@RunnerLifeCycleHandler(handler=SystemPropertySetter.class, phase=RunnerLifeCycle.ALL)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface SystemPropertiesSetter {
	String name();
	String value();
	RunnerLifeCycle[] phase();
	SetPropertyCondition condition() default SetPropertyCondition.always;
	String conditionValue() default "##default##";
}

