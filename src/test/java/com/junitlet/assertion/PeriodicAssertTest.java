package com.junitlet.assertion;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.junit.Test;

public class PeriodicAssertTest {
	/**
	 * Test takes 100 ms, assertion is limited by 1000 ms, but the hole test is limited
	 * by 300 ms (just in case). Anyway test takes less time than the assertion limit.
	 */
	@Test(timeout = 300)
	public void delayedTest() {
		delayedTest(100, 1000);
	}

	/**
	 * The test takes more time th
	 */
	@Test(timeout = 200, expected=AssertionError.class)
	public void tooLongTest() {
		delayedTest(100, 10);
	}
	
	
	private void delayedTest(final long testDelay, final long assertDelay) {
		final AtomicInteger state = new AtomicInteger(0);
		
		Executors.newSingleThreadExecutor().submit(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(testDelay);
					state.set(1);
				} catch (InterruptedException e) {
					throw new IllegalStateException(e);
				}
			}
		});
		
		new PeriodicAssert(assertDelay) {
			@Override
			public void run() {
				Assert.assertEquals(1, state.get());
			}
		};
	}

}
