package com.junitlet.assertion;

class AssertType {
	private Class<?> type;
	private boolean applyForSubClasses;
	
	AssertType(Class<?> type, boolean applyForSubClasses) {
		this.type = type;
		this.applyForSubClasses = applyForSubClasses;
	}

	public Class<?> getType() {
		return type;
	}

	public boolean isApplyForSubClasses() {
		return applyForSubClasses;
	}
	
	
	
}