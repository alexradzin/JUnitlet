package com.junitlet.arguments;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.runner.RunWith;

import com.junitlet.annotation.ConfigurableTest;
import com.junitlet.annotation.TestConfigurer;
import com.junitlet.runner.TestScenarioRunner;

@RunWith(TestScenarioRunner.class)
public class SingleConfigurableTestTest {
	private static Collection<Integer> oneParams = Arrays.asList(1,2,3);
	private static Collection<Integer> oneResults = new ArrayList<Integer>();
	
	@TestConfigurer("one")
	public static Integer[] configuration() {
		return oneParams.toArray(new Integer[0]);
	}
	
	@ConfigurableTest("one")
	public static void test(int value) {
		oneResults.add(value);
	}
	
	@AfterClass
	public static void after() {
		assertEquals(oneParams, oneResults);
	}
}
