package com.junitlet.assertion;


public class BeanAssert<T> {

	public static <T> BeanAssert<T> createAssert(Class<T> clazz) {
		return new BeanAssert<T>();
	}

	
	public static void assertMatches(String message, Object expected, Object actual, AssertPolicy policy) {
		if (expected == null && actual == null) {
			return;
		}
		MatchingResult result = null;
		if (expected != null) {
			result = isMatching(expected, actual, policy);
			if (result.isSuccess()) {
				return;
			}
		}
		failNotEquals(message, expected, actual, result);
	}

	private static MatchingResult isMatching(Object expected, Object actual, AssertPolicy policy) {
		return policy.checkCompliency(expected, actual);
	}

	public static void assertMatches(Object expected, Object actual, AssertPolicy policy) {
		assertMatches(null, expected, actual, policy);
	}
	
	public static void failNotEquals(String message, Object expected, Object actual, MatchingResult result) {
		//
	}
	
	
	
	public void addPolicy(Class<?> clazz, boolean subClasses){
		//
	}
}
