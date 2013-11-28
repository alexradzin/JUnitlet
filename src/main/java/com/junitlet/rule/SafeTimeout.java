package com.junitlet.rule;

import org.junit.Test;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.rules.Timeout;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.junitlet.annotation.TestTimeout;
import com.junitlet.util.TestEnvironment;

/**
 * {@link Test} annotation allows defining test timeout per test. This gives control on
 * timeout of each specific test. However it does not allow to define default timeout for all tests
 * together. 
 * <br/>
 * 
 * {@link Timeout} rule let us to define default timeout for all tests that belong to specific test case.
 * This timeout can be then overridden using {@link Test#timeout()}.
 * <br/> 
 *  
 * Both ways do not allow to disable the timeout while debugging the code. This can be annoying. 
 * {@link SafeTimeout} provides this functionality. Just add the following rule definition to your test:
 * <code>
 * 	 @Rule
 *	 public final SafeTimeout timeout = new SafeTimeout(1000);	
 * </code>
 * 
 * and timeout of all tests becomes 1000 ms. Timeout of specific test can be overridden using separate 
 * annotation {@link TestTimeout} that can be used to define the test timeout hard coded and override it 
 * if it is needed using system property {@link TestTimeout#property()}.   
 * 
 *  
 * @author alexr
 *
 */
public class SafeTimeout extends Timeout {
	public SafeTimeout(int millis) {
		super(millis);
	}

	public Statement apply(Statement base, Description description) {
		if (TestEnvironment.isDebugging()) {
			return base;
		}
		
		TestTimeout testTimeout = description.getAnnotation(TestTimeout.class);
		if (testTimeout == null) {
			return super.apply(base, description);
		}
		
		int timeout = TestTimeout.TestTimoutExtractor.getTimeout(testTimeout);
		return new FailOnTimeout(base, timeout);
	}
}
