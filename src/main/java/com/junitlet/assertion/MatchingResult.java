package com.junitlet.assertion;

public class MatchingResult {
	private boolean success;
	private String message;
	private String position;
	private Object expected;
	private Object actual;
	
	
	public MatchingResult() {
		// default costructor
	}

	public MatchingResult(Object expected, Object actual, boolean success) {
		this.expected = expected;
		this.actual = actual;
		this.success = success;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Object getExpected() {
		return expected;
	}
	public void setExpected(Object expected) {
		this.expected = expected;
	}
	public Object getActual() {
		return actual;
	}
	public void setActual(Object actual) {
		this.actual = actual;
	}
	
	
	
}
