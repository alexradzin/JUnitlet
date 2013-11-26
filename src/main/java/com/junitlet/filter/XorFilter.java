package com.junitlet.filter;

import java.util.concurrent.Callable;

public class XorFilter implements Callable<Boolean> {
	private Callable<Boolean>[] subFilters;
	
	public XorFilter(Callable<Boolean>[] subFilters) {
		this.subFilters = subFilters;
	}

	@Override
	public Boolean call() throws Exception {
		boolean flag = false;
		for (Callable<Boolean> filter : subFilters) {
			if (filter.call()) {
				if (flag) {
					return false;
				}
				flag = true;
			}
		}
		return flag;
	}

}
