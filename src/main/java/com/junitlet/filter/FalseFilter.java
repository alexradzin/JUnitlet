package com.junitlet.filter;

import java.util.concurrent.Callable;

public class FalseFilter implements Callable<Boolean> {

	@Override
	public Boolean call() throws Exception {
		return false;
	}

}
