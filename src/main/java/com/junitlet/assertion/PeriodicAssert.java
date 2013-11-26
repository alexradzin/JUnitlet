package com.junitlet.assertion;



/**
 * Sometimes we have to pause test to ensure that asynchronous operations have
 * been completed before continue to validate the test results. The simplest way
 * to implement this is using {@link Thread#sleep(long)}. The problem is how
 * long to sleep? Too short delay might be not enough. Too long delay increases
 * the test execution time. <br/><br/>
 * 
 * This class provides simple and generic solution for the problem. It is
 * abstract class, implements {@link kRunnable} but does not implement
 * {@link Runnable#run()} itself, so user must do it. The provided custom
 * {@code run()} method should implement logic of result validation. Constructor
 * {@link #PeriodicAssert(long)} accepts time maximum sleep time. Important:
 * actual work is done in method {@link #start()} that tries to run method
 * {@code run()} (i.e. to validate the data) several times in loop with short
 * delay. It catches {@link AssertionError} on each iteration. If validation
 * succeeds it returns. If not the {@link AssertionError} is thrown.<br/><br/>
 * 
 * Bottom line: relatively to {@link Thread#sleep(long)} {@link PeriodicAssert}
 * saves time: in worse case it sleeps the specific time. In most cases it
 * sleeps probably only 10% of specified time. So, we can both save time needed
 * to run tests and make delay dependent tests more stable: we can safely
 * increase the delay. In most cases test will sleep less.<br/><br/>
 * 
 * It is recommended to use this class implementing custom validation as
 * anonymous inner class:
 *   <pre>
 *		new PeriodicAssert(5000) {
 *			{@literal @}Override
 *			public void run() {
 *				Assert.assertEquals(expected, actual);
 *			}
 *		};
 *   </pre>
 *
 * 
 * @author alexr
 */
public abstract class PeriodicAssert implements Runnable {
	private long sleep;
	private long quantum;

	
	public PeriodicAssert(long sleep, long quantum, boolean autostart) {
		this.sleep = sleep;
		this.quantum = quantum;
		
		if (autostart) {
			start();
		}
	}

	public PeriodicAssert(long sleep, long quantum) {
		this(sleep, quantum, true);
	}
	
	public PeriodicAssert(long sleep) {
		this(sleep, Math.max(sleep / 10, 10));
	}

	public void start() {
		long before = System.currentTimeMillis();
		long now = before;

		do {
			try {
				Thread.sleep(quantum);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}

			try {
				PeriodicAssert.this.run();
				return;
			} catch (AssertionError e) {
				// do nothing. It seems that the stuff is not ready yet.
			}
			now = System.currentTimeMillis();
		} while (now - before < sleep);

		// If we are here the assert never passed into the loop.
		// Run the code once again this time synchronously.
		// If it still throws error the test will fail.
		PeriodicAssert.this.run();
	}
}
