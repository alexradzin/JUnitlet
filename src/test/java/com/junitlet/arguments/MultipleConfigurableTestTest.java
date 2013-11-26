package com.junitlet.arguments;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.junit.AfterClass;
import org.junit.runner.RunWith;

import com.junitlet.annotation.ConfigurableTest;
import com.junitlet.annotation.TestConfigurer;
import com.junitlet.runner.TestScenarioRunner;


@RunWith(TestScenarioRunner.class)
public class MultipleConfigurableTestTest {
	private static Collection<Integer> oneParams = new HashSet<Integer>(Arrays.asList(1, 2, 3));
	private static Collection<Integer> oneResults = new HashSet<Integer>(new ArrayList<Integer>());
	private static Collection<Integer> twoParams = new HashSet<Integer>(Arrays.asList(4, 5, 6));
	private static Collection<Integer> twoResults = new HashSet<Integer>(new ArrayList<Integer>());
	private static Collection<Integer> oneTwoResults = new HashSet<Integer>(new ArrayList<Integer>());
	private static Collection<Integer> twoOneResults = new HashSet<Integer>(new ArrayList<Integer>());
	
	
	public MultipleConfigurableTestTest() {
		System.out.println();
	}
	
	@TestConfigurer("one")
	public static Integer[] configuration1() {
		return oneParams.toArray(new Integer[0]);
	}

	@TestConfigurer("two")
	public static Integer[] configuration2() {
		return twoParams.toArray(new Integer[0]);
	}
	
	@ConfigurableTest("one")
	public void test1(int value) {
		oneResults.add(value);
	}

	@ConfigurableTest("two")
	public void test2(int value) {
		twoResults.add(value);
	}

	@ConfigurableTest({"one", "two"})
	public void test12(int value) {
		oneTwoResults.add(value);
	}

	@ConfigurableTest({"two", "one"})
	public void test21(int value) {
		twoOneResults.add(value);
	}
	
	@AfterClass
	public static void after() {
		assertEquals(oneParams, oneResults);
		assertEquals(twoParams, twoResults);
		
		Collection<Integer> oneTwo = new HashSet<Integer>(oneParams);
		oneTwo.addAll(twoParams);
		assertEquals(oneTwo, oneTwoResults);
		assertEquals(oneTwo, twoOneResults);
	}
}
