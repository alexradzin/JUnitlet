package com.junitlet.filter;

import java.util.concurrent.Callable;

public class TrueFilter implements Callable<Boolean> {

	@Override
	public Boolean call() throws Exception {
		return true;
	}

}
