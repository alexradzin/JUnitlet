package com.junitlet.rule;

import java.util.Map;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * Logging rule allows easy logging of each test starting and finishing including the test result 
 * in unified form. This rule can be very useful when tests fail sometimes and the particular test 
 * result might depend on the order of tests running.
 * 
 * This rule instance should not be created by direct call of its constructor. More convenient way is to 
 * use {@link LoggingRuleBuilder} 
 *  
 * @author alexr
 *
 * @param <L>
 */
public class LoggingRule<L> extends TestWatcher {
	public enum Phase {
		starting, succeeded, failed, finished
	}
	
	private final Map<Phase, String> logPolicy;
	
	private final L logger;
	
	LoggingRule(Class<?> logFactory, String factoryMethod, String logName, Map<Phase, String> logPolicy) {
		this.logPolicy = logPolicy;
		
		try {
			@SuppressWarnings("unchecked")
			L l = (L)logFactory.getMethod(factoryMethod, String.class).invoke(null, logName);
			logger = l;
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException(e);
		}
	}


	protected void starting(Description description) {
		log(Phase.starting, null, description);
	}

	protected void succeeded(Description description) {
		log(Phase.succeeded, null, description);
	}
	
	protected void failed(Throwable e, Description description) {
		log(Phase.failed, e, description);
	}

	protected void finished(Description description) {
		log(Phase.finished, null, description);
	}
	
	private void log(Phase phase, Throwable e, Description description) {
		String level = logPolicy.get(phase);
		if (level == null) {
			return;
		}
		String message = description.getTestClass().getName() + "#" + description.getDisplayName() + ": is " + phase.name();
		boolean logDone = false;
		if (e != null) {
			logDone = log(level, message, e);
		} 
		
		if (!logDone) {
			logDone = log(level, message);
		}
	
		if (!logDone) {
			throw new UnsupportedOperationException("Cannot find log message " + message);
		}
	}
	
	private boolean log(String level, String message, Throwable e) {
		return logImpl(level, new Class[][] {{String.class, Throwable.class}}, new Object[] {message, e});
	}

	private boolean log(String level, String message) {
		return logImpl(level, new Class[][] {{String.class}, {String.class, String[].class}}, new Object[] {message});
	}
	
	private boolean logImpl(String methodName, Class<?>[][] prototypes, Object[] arguments) {
		Class<?> clazz = logger.getClass();
		for (Class<?>[] prototype : prototypes) {
			try {
				clazz.getMethod(methodName, prototype).invoke(logger, arguments);
				// if method was executed successfully, return. The work is done.
				return true;
			} catch (ReflectiveOperationException e) {
				continue; // try the next prototype
			}
		}
		return false;
	}
}
