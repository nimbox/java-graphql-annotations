package com.nimbox.graphql.types;

import java.lang.reflect.Method;

import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeParameterFactory;
import com.nimbox.graphql.runtime.RuntimeInstanceDataFetcher;

import graphql.schema.DataFetcher;

public class GraphQueryField extends GraphField {

	// constructor

	public GraphQueryField(final GraphRegistry registry, final Class<?> typeClass, final Method fieldMethod) {
		super(registry, typeClass, fieldMethod);
	}

	// methods

	@Override
	public DataFetcher<?> getFetcher(RuntimeParameterFactory factory) {
		return new RuntimeInstanceDataFetcher<>(factory, typeClass, fieldMethod, parameters);
	}

}
