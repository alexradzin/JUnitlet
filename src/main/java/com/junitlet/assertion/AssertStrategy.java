package com.junitlet.assertion;

import java.util.Collection;
import java.util.Map;

public enum AssertStrategy {
	REFERECE {
		@Override
		public MatchingResult verify(Object expected, Object actual) {
			return new MatchingResult(expected, actual, expected == actual);
		}
	}, 
	EQUALS {
		@Override
		public MatchingResult verify(Object expected, Object actual) {
			return new MatchingResult(expected, actual, expected.equals(actual));
		}
	},  
	/**
	 * Relevant for collections, maps and strings. Means that actual contains expected.
	 */
	CONTAINS {
		@Override
		public MatchingResult verify(Object expected, Object actual) {
			return null;
		}
	},  
	/**
	 * Relevant for collections. The same as {@link #EQUALS} for 2 sets.
	 * Compares lists by creating 2 sets.
	 */
	ORDER_INDEPENDENT {
		@Override
		public MatchingResult verify(Object expected, Object actual) {
			if (expected instanceof Collection || expected instanceof Map) {
				//
			}
			
			
			return null;
		}
	}, 
	;
	
	public abstract MatchingResult verify(Object expected, Object actual);
}