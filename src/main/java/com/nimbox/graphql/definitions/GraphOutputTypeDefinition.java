package com.nimbox.graphql.definitions;

import java.lang.reflect.Method;

import com.nimbox.graphql.registries.GraphRegistry;

public class GraphOutputTypeDefinition extends GraphWrappedTypeDefinition {

	// constructors

	public GraphOutputTypeDefinition(final GraphRegistry registry, final Method method) {
		super(new Builder(registry, method, method.getGenericReturnType()));
	}

}
