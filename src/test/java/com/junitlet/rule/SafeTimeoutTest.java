package com.junitlet.rule;
import org.junit.Rule;
import org.junit.Test;

import com.junitlet.annotation.TestTimeout;
import com.junitlet.rule.SafeTimeout;


public class SafeTimeoutTest {
	 @Rule
	 public final SafeTimeout timeout = new SafeTimeout(1000);	
	
	
	@Test
	public void testWithGloballyDefinedTimeout() throws InterruptedException {
		Thread.sleep(500);
	}
		
		
	@Test
	@TestTimeout(2000)
	public void testWithProprietaryTimeout() throws InterruptedException {
		Thread.sleep(1500);
	}
}
