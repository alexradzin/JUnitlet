package com.junitlet.runner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.junitlet.annotation.SystemPropertiesSetter;
import com.junitlet.annotation.SystemPropertiesSetters;

public class SystemPropertySetter implements RunnerLifeCycleWorker {
	@Override
	public void handle(Class<?> testClass, RunnerLifeCycle phase) {
		SystemPropertiesSetter setter = testClass.getAnnotation(SystemPropertiesSetter.class);
		
		SystemPropertiesSetters setters = testClass.getAnnotation(SystemPropertiesSetters.class);
	
		
		List<SystemPropertiesSetter> allSetters = new ArrayList<SystemPropertiesSetter>();
		if (setter != null) {
			allSetters.add(setter);
		}
		if (setters != null) {
			allSetters.addAll(Arrays.asList(setters.value()));
		}
		
		
		for (SystemPropertiesSetter s : allSetters) {
			if(Arrays.asList(s.phase()).contains(phase) || Arrays.asList(s.phase()).contains(RunnerLifeCycle.ALL)) {
				System.setProperty(s.name(), s.value());
			}
		}
	}

}
