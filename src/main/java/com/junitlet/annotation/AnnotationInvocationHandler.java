package com.junitlet.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

class AnnotationInvocationHandler<A extends Annotation> implements InvocationHandler {
	private AnnotationExtractor extractor;
	private A primary;
	private Annotation[] secondaries;

	// annotations in reverse order: reversed secondary annotations, then primary annotation.
	private Annotation[] annotations;
	
	AnnotationInvocationHandler(AnnotationExtractor extractor, A annotation, Annotation ... secondary) {
		this.extractor = extractor;
		this.primary = annotation;
		this.secondaries = secondary;
		
		if (secondaries == null) {
			annotations = new Annotation[] {primary};
			return;
		}
		
		int n = 1 + secondary.length;
		annotations = new Annotation[n];
		for (int i = 0; i < secondary.length;  i++) {
			annotations[n - i - 2] = secondaries[i];
		}
		annotations[n - 1] = primary;
	}
	
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (args != null && args.length != 0) {
			throw new IllegalArgumentException("Annotation methods cannot accept arguments");
		}

		for (Annotation a : annotations) {
			try {
				Method m = a.getClass().getMethod(method.getName());
				if (!method.getReturnType().equals(m.getReturnType())) {
					continue;
				}
				// Method is found. Since the annotations list is reversed the first found annotation 
				// that has required method dictates the value. 
				//TODO: add code here that transforms domain specific annotations to 
				// framework annotations if method returns an array of annotations.
				return transform(m.invoke(a), m.getReturnType());
			} catch (NoSuchMethodException e) {
				// there is not such method. continue...
			}
		}
		
		throw new NoSuchMethodException(method.toString());
	}

	
	@SuppressWarnings("unchecked")
	private Object transform(Object src, Class<?> clazz) {
		if (clazz.isAnnotation()) {
			if (clazz.isAssignableFrom(src.getClass())) {
				return src;
			}
			return extractor.getAnnotationAnnotation((Annotation)src, (Class<? extends Annotation>)clazz);
		}

		if (clazz.isArray() && clazz.getComponentType().isAnnotation()) {
			if (clazz.getComponentType().isAssignableFrom(src.getClass().getComponentType())) {
				return src;
			}
			
			int n = Array.getLength(src);
			Object array = Array.newInstance(clazz.getComponentType(), n);
			for (int i = 0;  i < n;  i++) {
				Annotation transformedAnnotation = 
						extractor.getAnnotationAnnotation(
								(Annotation)Array.get(src, i), 
								(Class<? extends Annotation>)clazz);
				Array.set(array, i, transformedAnnotation);
			}
			return array;
		}
		
		return src;
	}
}
