package com.junitlet.runner;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.junitlet.annotation.SystemPropertiesSetter;
import com.junitlet.annotation.SystemPropertiesSetters;

@RunWith(LifeCycleRunner.class)
@SystemPropertiesSetters({
	@SystemPropertiesSetter(name = "mytestprop", value = "mytestvalue", phase = RunnerLifeCycle.BEFORE_CONSTRUCTOR)
})
public class SystemPropertySettersTest {
	@Test
	public void test() {
		Assert.assertEquals("mytestvalue", System.getProperty("mytestprop"));
	}
}
