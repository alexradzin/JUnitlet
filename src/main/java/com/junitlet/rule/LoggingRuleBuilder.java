package com.junitlet.rule;

import java.util.HashMap;
import java.util.Map;

import com.junitlet.rule.LoggingRule.Phase;

public class LoggingRuleBuilder<L> {
	private Class<L> logFactory;
	private String factoryMethod = "getLogger";
	private String logName;
	private Map<Phase, String> logPolicy = new HashMap<>();
	
	private final static Map<Phase, String> defaultLogPolicy = new HashMap<Phase, String>() {{
		put(Phase.starting, "info");
		put(Phase.succeeded, "info");
		put(Phase.failed, "error");
	}};
	
	
	
	public LoggingRuleBuilder<L> with(Class<L> logFactory) {
		this.logFactory = logFactory;
		return this;
	}
	
	public LoggingRuleBuilder<L> using(String factoryMethod) {
		this.factoryMethod = factoryMethod;
		return this;
	}
	
	public LoggingRuleBuilder<L> named(String logName) {
		this.logName = logName;
		return this;
	}

	public LoggingRuleBuilder<L> log(Phase phase, String level) {
		this.logPolicy.put(phase, level);
		return this;
	}
	
	public LoggingRule<L> build() {
		if (logFactory == null) {
			throw new IllegalStateException("logFactory is mandatory parameter. Call builder.with(logFactoryClass)");
		}
		
		Map<Phase, String> currentLogPolicy = logPolicy.isEmpty() ? defaultLogPolicy : logPolicy;
		String currentLogName = logName;
		if (currentLogName == null) {
			for (StackTraceElement e : new Throwable().getStackTrace()) {
				if (e.getClassName().equals(getClass().getName())) {
					continue;
				}
				currentLogName = e.getClassName();
				break;
			}
		}
		return new LoggingRule<L>(logFactory, factoryMethod, currentLogName, currentLogPolicy);
	}
}
