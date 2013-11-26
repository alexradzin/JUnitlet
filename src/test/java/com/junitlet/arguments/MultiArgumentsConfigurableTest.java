package com.junitlet.arguments;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.runner.RunWith;

import com.junitlet.annotation.ConfigurableTest;
import com.junitlet.annotation.TestConfigurer;
import com.junitlet.runner.TestScenarioRunner;

@RunWith(TestScenarioRunner.class)
public class MultiArgumentsConfigurableTest {
	private static Object[][] oneParams = new Object[][] {
		new Object[] {1, "first"},
		new Object[] {2, "second"},
		new Object[] {3, "third"},
	};
	private static List<List<Object>> expectedResults = new ArrayList<List<Object>>() {{
		add(Arrays.<Object>asList(1, "first"));
		add(Arrays.<Object>asList(2, "second"));
		add(Arrays.<Object>asList(3, "third"));
	}};
	private static Collection<List<Object>> oneResults = new ArrayList<List<Object>>();
	
	@TestConfigurer("one")
	public static Object[][] configuration() {
		return oneParams;
	}
	
	@ConfigurableTest("one")
	public void test(int i, String s) {
//		oneResults.add(new Object[] {i, s});
		oneResults.add(Arrays.<Object>asList(i, s));
	}
	
	@AfterClass
	public static void after() {
//		Set<Object[]> p = new HashSet<Object[]>(Arrays.asList(oneParams));
		assertEquals(expectedResults, oneResults);
	}
	
}
