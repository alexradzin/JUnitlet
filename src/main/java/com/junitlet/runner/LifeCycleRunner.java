package com.junitlet.runner;

import java.lang.annotation.Annotation;
import java.util.EnumMap;
import java.util.Map;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

import com.junitlet.annotation.AnnotationExtractor;
import com.junitlet.annotation.RunnerLifeCycleHandler;
import com.junitlet.annotation.RunnerLifeCycleHandlers;

public class LifeCycleRunner extends RunnerWrapperRunner {
	private Map<RunnerLifeCycle, RunnerLifeCycleWorker> runnerHandlers;
	private Map<RunnerLifeCycle, RunnerLifeCycleWorker> testCaseHandlers;
	
	private Class<?> testClass;
	
	private AnnotationExtractor annotationExtractor;
	
	
	public LifeCycleRunner(Class<?> clazz) {
		super(clazz);
		handle(RunnerLifeCycle.AFTER_CONSTRUCTOR);
	}

	public LifeCycleRunner(Runner runner) {
		super(runner);
	}

	@Override
	public void run(RunNotifier notifier) {
		handle(RunnerLifeCycle.BEFORE_RUN);
		super.run(notifier);
		handle(RunnerLifeCycle.AFTER_RUN);
	}
	
	
	private void handle(RunnerLifeCycle phase) {
		RunnerLifeCycleWorker handler = testCaseHandlers.get(phase);
		if (handler == null) {
			handler = runnerHandlers.get(phase);
		}
				
		handle(testClass, phase, handler);
	}
	
	
	protected  void handle(Class<?> clazz, RunnerLifeCycle phase, RunnerLifeCycleWorker handler) {
		if (handler != null) {
			try {
				handler.handle(clazz, phase);
			} catch (Exception e) {
				e.printStackTrace();
				throw new AssertionError("Execution of runner lifecycle handler failed on phase " + phase + ":" + e.getMessage());
			}
		}
	}

	@Override
	protected void beforeConstruction(Class<?> clazz) {
		annotationExtractor = new AnnotationExtractor();
		runnerHandlers = new EnumMap<RunnerLifeCycle, RunnerLifeCycleWorker>(RunnerLifeCycle.class);
		testCaseHandlers = new EnumMap<RunnerLifeCycle, RunnerLifeCycleWorker>(RunnerLifeCycle.class);
		testClass = clazz;
		initHandlers(annotationExtractor.getAnnotation(getClass(), RunnerLifeCycleHandler.class), runnerHandlers);
		initHandlers(annotationExtractor.getAnnotation(clazz, RunnerLifeCycleHandler.class), testCaseHandlers);
		
		initHandlers(annotationExtractor.getAnnotation(getClass(), RunnerLifeCycleHandlers.class), runnerHandlers);
		initHandlers(annotationExtractor.getAnnotation(clazz, RunnerLifeCycleHandlers.class), testCaseHandlers);
		
		
		handle(RunnerLifeCycle.BEFORE_CONSTRUCTOR);
	}


	
	protected void initHandlers(RunnerLifeCycleHandlers ac, Map<RunnerLifeCycle, RunnerLifeCycleWorker> handlers) {
		if (ac == null) {
			return;
		}
		for (Annotation a : ac.value()) {
			if (!(a instanceof RunnerLifeCycleHandler)) {
				Annotation annotationAnnotation = a.annotationType().getAnnotation(RunnerLifeCycleHandler.class);
				if (annotationAnnotation != null) {
					a = annotationExtractor.mergeAnnotations(annotationAnnotation, a);
				}
			}
			initHandlers((RunnerLifeCycleHandler)a, handlers);
		}
	}
	
	
	
	protected void initHandlers(RunnerLifeCycleHandler a, Map<RunnerLifeCycle, RunnerLifeCycleWorker> handlers) {
		try {
			if (a == null) {
				return;
			}
			
			RunnerLifeCycle[] phases = a.phase();
			for (RunnerLifeCycle phase : phases) {
				if (RunnerLifeCycle.ALL.equals(phase)) {
					phases = new RunnerLifeCycle[RunnerLifeCycle.values().length - 1];
					int i = 0;
					for (RunnerLifeCycle p : RunnerLifeCycle.values()) {
						if (!RunnerLifeCycle.ALL.equals(p)) {
							phases[i] = p;
							i++;
						}
					}
					break;
				}
			}
			
			for (RunnerLifeCycle phase : phases) {
				Class<? extends RunnerLifeCycleWorker> type = a.handler();
				RunnerLifeCycleWorker handler = type.newInstance();
				handlers.put(phase, handler);
			}
		} catch (InstantiationException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}
	
	
//	private <A extends Annotation> A getAnnotation(Class<?> clazz, Class<A> annotationClass) {
//		A typeAnnotation = clazz.getAnnotation(annotationClass);
//		if (typeAnnotation != null) {
//			return typeAnnotation;
//		}
//		
//		for (Annotation a : clazz.getAnnotations()) {
//			A annotationAnnotation = a.annotationType().getAnnotation(annotationClass);
//			if (annotationAnnotation != null) {
//				return mergeAnnotations(annotationAnnotation, a);
//			}
//		}
//		
//		
//		for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
//			for (Class<?> ic : c.getInterfaces()) {
//				typeAnnotation = getAnnotation(ic, annotationClass);
//				if (typeAnnotation != null) {
//					return typeAnnotation;
//				}
//			}
//		}
//	
//		return null;
//	}
//	
//	
//	@SuppressWarnings("unchecked")
//	private <A extends Annotation> A mergeAnnotations(A primary, Annotation secondary) {
//		return (A)Proxy.newProxyInstance(
//				getClass().getClassLoader(), 
//				new Class[] {primary.annotationType()}, 
//				new AnnotationInvocationHandler<Annotation>(primary, secondary));
//	}
}
