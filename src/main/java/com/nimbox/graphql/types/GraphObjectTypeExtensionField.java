package com.nimbox.graphql.types;

import java.lang.reflect.Method;

import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeParameterFactory;
import com.nimbox.graphql.runtime.RuntimeSourceExtensionDataFetcher;

import graphql.schema.DataFetcher;

public class GraphObjectTypeExtensionField extends GraphField {

	// properties

	private Class<?> objectTypeClass;

	// constructor

	public GraphObjectTypeExtensionField(final GraphRegistry registry, final Class<?> typeClass, final Method fieldMethod, Class<?> objectTypeClass) {
		super(registry, typeClass, fieldMethod);
		this.objectTypeClass = objectTypeClass;
	}

	// methods

	public DataFetcher<?> getFetcher(RuntimeParameterFactory factory) {
		return new RuntimeSourceExtensionDataFetcher<>(factory, typeClass, fieldMethod, parameters, objectTypeClass);
	}

}
