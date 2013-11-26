package com.junitlet.filter;

import java.util.concurrent.Callable;

public class AndFilter implements Callable<Boolean> {
	private Callable<Boolean>[] subFilters;
	
	public AndFilter(Callable<Boolean>[] subFilters) {
		this.subFilters = subFilters;
	}

	@Override
	public Boolean call() throws Exception {
		for (Callable<Boolean> filter : subFilters) {
			if (!filter.call()) {
				return false;
			}
		}
		return true;
	}

}
