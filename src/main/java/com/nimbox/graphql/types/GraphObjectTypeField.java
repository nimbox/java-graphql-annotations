package com.nimbox.graphql.types;

import java.lang.reflect.Method;

import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeParameterFactory;
import com.nimbox.graphql.runtime.RuntimeSourceDataFetcher;

import graphql.schema.DataFetcher;

public class GraphObjectTypeField extends GraphField {

	// constructor

	public GraphObjectTypeField(final GraphRegistry registry, final Class<?> typeClass, final Method fieldMethod) {
		super(registry, typeClass, fieldMethod);
	}

	// methods

	public DataFetcher<?> getFetcher(RuntimeParameterFactory factory) {
		return new RuntimeSourceDataFetcher<>(factory, typeClass, fieldMethod, parameters);
	}

}
