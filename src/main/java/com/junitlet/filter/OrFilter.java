package com.junitlet.filter;

import java.util.concurrent.Callable;

public class OrFilter implements Callable<Boolean> {
	private Callable<Boolean>[] subFilters;
	
	public OrFilter(Callable<Boolean>[] subFilters) {
		this.subFilters = subFilters;
	}

	@Override
	public Boolean call() throws Exception {
		for (Callable<Boolean> filter : subFilters) {
			if (filter.call()) {
				return true;
			}
		}
		return false;
	}

}
