package com.junitlet.filter;

import static org.junit.Assert.fail;

import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.junitlet.annotation.Argument;
import com.junitlet.annotation.Condition;
import com.junitlet.runner.ConditionalRunner;

@RunWith(ConditionalRunner.class)
public class PatternFilterNegativeTest {
	@Condition(filter=PropertyPatternFilter.class, arguments={
		@Argument(type=String.class, value="os.name"),
		@Argument(type=Pattern.class, value="foobar*")
	})
	@Test
	public void testConditionShouldNotRun() {
		fail();
	}
	
	
}
