package com.junitlet.filter;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Pattern;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.junitlet.annotation.Argument;
import com.junitlet.annotation.Condition;
import com.junitlet.annotation.Conditions;
import com.junitlet.runner.ConditionalRunner;

@RunWith(ConditionalRunner.class)
public class PatternFilterPositiveTest {
	private static Collection<String> performedTests = new HashSet<String>();
	
	
	@Condition(filter=PropertyPatternFilter.class, arguments={
		@Argument(type=String.class, value="os.name"),
		@Argument(type=Pattern.class, value=".*[Ww]indows|[Ll]inux.*")
	})
	@Test
	public void testConditionWithArgs() {
		register();
	}

	@Condition(filter=PropertyPatternFilter.class, arguments={
		@Argument(type=String.class, value="os.name"),
		@Argument(type=Pattern.class, factoryMethod="compile", value=".*[Ww]indows|[Ll]inux.*")
	})
	
	@Conditions(
			@Condition(filter=PropertyPatternFilter.class, arguments={
				@Argument(type=String.class, value="os.name"),
				@Argument(type=Pattern.class, value=".*[Ww]indows|[Ll]inux.*", integer = Pattern.DOTALL | Pattern.CASE_INSENSITIVE)
			})
	)
	@Test
	public void testConditionsOneConditionWithArgs() {
		register();
	}

	
	@Conditions({
			@Condition(filter=PropertyPatternFilter.class, arguments={
				@Argument(type=String.class, value="os.name"),
				@Argument(type=Pattern.class, value=".*[Ww]indows.*")
			}),
			@Condition(filter=PropertyPatternFilter.class, arguments={
				@Argument(type=String.class, value="os.name"),
				@Argument(type=Pattern.class, value=".*[Ll]inux.*")
			})}
	)
	@Test
	public void testConditionsTwoConditionsDefaultRelationWithArgs() {
		register();
	}
	
	
	@Conditions(
		relation = Conditions.Relation.OR,
		value = {
		@Condition(filter=PropertyPatternFilter.class, arguments={
			@Argument(type=String.class, value="os.name"),
			@Argument(type=Pattern.class, value=".*[Ww]indows.*")
		}),
		@Condition(filter=PropertyPatternFilter.class, arguments={
			@Argument(type=String.class, value="os.name"),
			@Argument(type=Pattern.class, value=".*[Ll]inux.*")
		})}
	)
	@Test
	public void testConditionsTwoConditionsExplicitRelationOrWithArgs() {
		register();
	}

	@Conditions(
		relation = Conditions.Relation.AND,
		value = {
			@Condition(filter=PropertyPatternFilter.class, arguments={
				@Argument(type=String.class, value="os.name"),
				@Argument(type=Pattern.class, value=".*[Ww]indows|[Ll]inux.*")
			}),
			@Condition(filter=PropertyPatternFilter.class, arguments={
				@Argument(type=String.class, value="user.name"),
				@Argument(type=Pattern.class, value="^.+$")
			})
		}
	)
	@Test
	public void testConditionsTwoConditionsExplicitRelationAndWithArgs() {
		register();
	}

	private static void register() {
		String testName = new Throwable().getStackTrace()[1].getMethodName();
		performedTests.add(testName);
	}
	
	
	@AfterClass
	public static void afterAll() {
		for (Method m : PatternFilterPositiveTest.class.getMethods()) {
			int flags = m.getModifiers();
			if (void.class.equals(m.getReturnType()) && 
					Modifier.isPublic(flags) && 
					!Modifier.isStatic(flags) && 
					m.getAnnotation(Test.class) != null) {
				assertTrue(m.getName(), performedTests.contains(m.getName()));
			}
		}
	}
}
