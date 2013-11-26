package com.junitlet.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;


public class AnnotationExtractor {

	public <A extends Annotation> A getAnnotation(Class<?> clazz, Class<A> annotationClass) {
		A typeAnnotation = clazz.getAnnotation(annotationClass);
		if (typeAnnotation != null) {
			return typeAnnotation;
		}
		
		for (Annotation a : clazz.getAnnotations()) {
			A annotationAnnotation = getAnnotationAnnotation(a, annotationClass);
			if (annotationAnnotation != null) {
				return annotationAnnotation;
			}
//			A annotationAnnotation = a.annotationType().getAnnotation(annotationClass);
//			if (annotationAnnotation != null) {
//				return mergeAnnotations(annotationAnnotation, a);
//			}
		}
		
		
		for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
			for (Class<?> ic : c.getInterfaces()) {
				typeAnnotation = getAnnotation(ic, annotationClass);
				if (typeAnnotation != null) {
					return typeAnnotation;
				}
			}
		}
	
		return null;
	}
	
	public <A extends Annotation> A getAnnotationAnnotation(Annotation a, Class<A> annotationClass) {
		A annotationAnnotation = a.annotationType().getAnnotation(annotationClass);
		if (annotationAnnotation != null) {
			return mergeAnnotations(annotationAnnotation, a);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <A extends Annotation> A mergeAnnotations(A primary, Annotation secondary) {
		return (A)Proxy.newProxyInstance(
				getClass().getClassLoader(), 
				new Class[] {primary.annotationType()}, 
				new AnnotationInvocationHandler<Annotation>(this, primary, secondary));
	}

}
