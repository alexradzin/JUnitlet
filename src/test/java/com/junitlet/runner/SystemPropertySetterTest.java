package com.junitlet.runner;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.junitlet.annotation.SystemPropertiesSetter;

@RunWith(LifeCycleRunner.class)
@SystemPropertiesSetter(name = "mytestprop", value = "mytestvalue", phase = RunnerLifeCycle.BEFORE_CONSTRUCTOR)
public class SystemPropertySetterTest {
	@Test
	public void test() {
		Assert.assertEquals("mytestvalue", System.getProperty("mytestprop"));
	}
}
