package com.junitlet.filter;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import com.junitlet.annotation.Argument;
import com.junitlet.annotation.Argument.ValueExtractor;
import com.junitlet.annotation.Condition;
import com.junitlet.annotation.Conditions;
import com.junitlet.annotation.Conditions.Relation;
import com.junitlet.util.InstanceCreator;

public class FilterFactory {

	public Callable<Boolean> createFilter(Method test) {
		Conditions conditions = test.getAnnotation(Conditions.class);
		if (conditions != null) {
			Condition[] childConditions = conditions.value();
			@SuppressWarnings("unchecked")
			Callable<Boolean>[]  subFilters = new Callable[childConditions.length];  
			for (int i = 0;  i < subFilters.length;  i++) {
				subFilters[i] = createFilter(childConditions[i]);
			}
			
			if (subFilters.length == 1) {
				return subFilters[0];
			}
			
			Relation relation = conditions.relation();
			switch (relation) {
				case AND:
					return new AndFilter(subFilters);
				case OR:
					return new OrFilter(subFilters);
				case XOR:
					return new XorFilter(subFilters);
				default:
					throw new IllegalArgumentException("Unsupported relation between conditions: " + relation);
			}
		}
		
		Condition condition = test.getAnnotation(Condition.class);
		if (condition != null) {
			return createFilter(condition);
		}
		
		return null;
	}

	
	private Callable<Boolean> createFilter(Condition condition) {
		Class<? extends Callable<Boolean>> filterClass = condition.filter();
		Argument[] arguments = condition.arguments();
		
		Object[] args = ValueExtractor.getValues(arguments);
		InstanceCreator<?, ? extends Callable<Boolean>> f = InstanceCreator.createCreator(filterClass, null, null, true, args);
		
		Callable<Boolean> filter = f.findCreator().call(args);
		return filter;
	}
}



