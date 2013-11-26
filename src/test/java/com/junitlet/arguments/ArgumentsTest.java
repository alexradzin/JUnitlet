package com.junitlet.arguments;

import java.util.regex.Pattern;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.junitlet.annotation.Argument;
import com.junitlet.annotation.TestConfiguration;
import com.junitlet.annotation.TestConfigurations;
import com.junitlet.runner.TestScenarioRunner;

@RunWith(TestScenarioRunner.class)
public class ArgumentsTest {
	@TestConfigurations({
		@TestConfiguration(name="even", arguments={@Argument(integer=6)}),
		@TestConfiguration(name="odd", arguments={@Argument(integer=7)}, expected=AssertionFailedError.class),
		@TestConfiguration(arguments={@Argument(integer=6)})
	})
	private void scenario(int n) {
		Assert.assertEquals(0, n%2);
	}

	
	
	@TestConfigurations({
		@TestConfiguration(name="even", arguments={@Argument(integer=6)}),
	})
	private void longScenario(long n) {
		Assert.assertEquals(0, n%2);
	}

	@TestConfigurations({
		@TestConfiguration(name="even", arguments={@Argument(number=6.0)}),
	})
	private void doubleScenario(double n) {
		Assert.assertEquals(0, ((int)n)%2);
	}

	@TestConfigurations({
		@TestConfiguration(name="even", arguments={@Argument(number=6.0)}),
	})
	private void floatScenario(float n) {
		Assert.assertEquals(0, ((int)n)%2);
	}

	
	@TestConfigurations({
		@TestConfiguration(name="eq", arguments={@Argument(value="hello"), @Argument(value="hello")}),
		@TestConfiguration(name="ne", arguments={@Argument(value="hello"), @Argument(value="bye")}, expected=AssertionFailedError.class),
	})
	private void stringScenario(String one, String two) {
		Assert.assertEquals(one, two);
	}
	
	@TestConfigurations({
		@TestConfiguration(name="match", arguments={
				@Argument(type=Pattern.class, factoryMethod="compile", value="^\\d+$"), 
				@Argument(value="123"), 
				@Argument(type=Boolean.class, value="true")}),
		@TestConfiguration(name="notmatch", arguments={
				@Argument(type=Pattern.class, factoryMethod="compile", value="^\\d+$"), 
				@Argument(value="123.456"), 
				@Argument(type=Boolean.class, value="false")}),
	})
	private void patternScenario(Pattern pattern, String str, boolean match) {
		Assert.assertEquals(match, pattern.matcher(str).find());
	}
	
	@Test
	public void regularTest() {
		Assert.assertTrue(true);
	}
	
	@TestConfigurations({
		@TestConfiguration(arguments={@Argument(integer=2)}, timeout=11),
	})
	public void timeoutTest(long t) throws InterruptedException {
			Thread.sleep(t);
	}
	
}
