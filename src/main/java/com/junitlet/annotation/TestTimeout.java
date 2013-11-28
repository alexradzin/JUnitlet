package com.junitlet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.junitlet.rule.SafeTimeout;

/**
 * This annotation allows separate definition of test timeout.
 * This annotations must be used in conjunction with @link {@link SafeTimeout} rule. 
 * In addition to built-in JUnit mechanism of tests timeouts this annotation lets us  
 * to override hard coded timeout by using system property defined by {@link TestTimeout#property()}.
 * @author alexr
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
@Documented
public @interface TestTimeout {
	/**
	 * The timeout value (ms)
	 * @return timeout
	 */
	public int value() default 0;
	
	/**
	 * Name of system property that allows overriding of hard coded defined value of test timeout.  
	 * @return system property name
	 */
	public String property() default "test.timeout";
	
	public static class TestTimoutExtractor {
		public static int getTimeout(TestTimeout testTimeout) {
			String propertyTimeout = System.getProperty(testTimeout.property());
			if (propertyTimeout != null) {
				return Integer.parseInt(propertyTimeout);
			}
			return testTimeout.value();
		}
	}
}
