package com.junitlet.assertion;


@SuppressWarnings("unused")
public class AssertPolicy {
	private AssertType type;
	private String attribute;
	private AssertType atributeType;
	private AssertStrategy strategy;
	
	
	public MatchingResult checkCompliency(Object expected, Object actual) {
		return null;
	}
}