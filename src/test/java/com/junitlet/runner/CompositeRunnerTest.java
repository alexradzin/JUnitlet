package com.junitlet.runner;


import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.junitlet.annotation.Argument;
import com.junitlet.annotation.Condition;
import com.junitlet.annotation.SystemPropertiesSetter;
import com.junitlet.annotation.WrapRunner;
import com.junitlet.filter.PropertyPatternFilter;

@RunWith(RunnerWrapperRunner.class)
@WrapRunner({SystemPropertySettingRunner.class, ConditionalRunner.class})
@SystemPropertiesSetter(name = "mytestprop1", value = "mytestvalue", phase = RunnerLifeCycle.BEFORE_RUN)
public class CompositeRunnerTest {
	private static boolean testConditionShouldRunExecuted = false;
	
	public CompositeRunnerTest() {
		testConditionShouldRunExecuted = false;
	}
	
	@Test
	public void test() {
		Assert.assertEquals("mytestvalue", System.getProperty("mytestprop1"));
	}

	
	@Condition(filter=PropertyPatternFilter.class, arguments={
		@Argument(type=String.class, value="mytestprop1"),
		@Argument(type=Pattern.class, value="mytestvalue")
	})
	@Test
	public void testConditionShouldRun() {
		testConditionShouldRunExecuted = true;
	}


	@Condition(filter=PropertyPatternFilter.class, arguments={
		@Argument(type=String.class, value="mytestprop"),
		@Argument(type=Pattern.class, value="othertestvalue")
	})
	@Test
	public void testConditionShouldNotRun() {
		fail();
	}
	
	@AfterClass
	public static void tierDown() {
		assertTrue(testConditionShouldRunExecuted);
	}
}
