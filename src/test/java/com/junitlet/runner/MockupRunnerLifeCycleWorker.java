package com.junitlet.runner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MockupRunnerLifeCycleWorker implements RunnerLifeCycleWorker {
	private static Map<Class<?>, Collection<Event>> events = new HashMap<Class<?>, Collection<Event>>(); 

	@Override
	public void handle(Class<?> testClass, RunnerLifeCycle phase) {
		handle(testClass, phase.name());
	}


	static void handle(Class<?> testClass, String phaseName) {
		Collection<Event> classEvents = events.get(testClass);
		if (classEvents == null) {
			classEvents = new ArrayList<Event>();
			events.put(testClass, classEvents);
		}
		classEvents.add(new Event(testClass, phaseName));
	}
	
	static void clear(Class<?> testClass) {
		events.remove(testClass);
	}
	
	static void clear() {
		events.clear();
	}
	
	static Collection<Event> getHandledEvents(Class<?> clazz) {
		return events.get(clazz);
	}
	
	
	static class Event {
		private Class<?> testClass;
		private String phaseName;
		
		Event(Class<?> testClass, String phaseName) {
			this.testClass = testClass;
			this.phaseName = phaseName;
		}

		public Class<?> getTestClass() {
			return testClass;
		}

		public String getPhaseName() {
			return phaseName;
		}
		
		@Override
		public String toString() {
			return testClass + "#" + phaseName;
		}
		
		@Override
		public int hashCode() {
			return 69 * testClass.hashCode() + phaseName.hashCode(); 
		}
		
		@Override
	    public boolean equals(Object obj) {
	    	if (this == obj) {
	    		return true;
	    	}
	    	if (!(obj instanceof Event)) {
	    		return false;
	    	}
	    	Event other = (Event)obj;
	    	
	    	return testClass.equals(other.getTestClass()) && phaseName.equals(other.getPhaseName());
	    }
	}
}
