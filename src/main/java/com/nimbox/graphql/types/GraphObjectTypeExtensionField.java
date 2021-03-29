package com.nimbox.graphql.types;

import java.lang.reflect.Method;

import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeParameterFactory;
import com.nimbox.graphql.runtime.RuntimeSourceExtensionDataFetcher;

import graphql.schema.DataFetcher;

public class GraphObjectTypeExtensionField extends GraphObjectTypeField {

	// properties

	private Class<?> sourceObjectTypeClass;

	// constructor

	public GraphObjectTypeExtensionField(final GraphRegistry registry, final Class<?> typeClass, final Method fieldMethod, Class<?> sourceObjectTypeClass) {
		super(registry, typeClass, fieldMethod);
		this.sourceObjectTypeClass = sourceObjectTypeClass;
	}

	// methods

	@Override
	public DataFetcher<?> getFetcher(RuntimeParameterFactory factory) {
		return new RuntimeSourceExtensionDataFetcher<>(factory, container, method, parameters, sourceObjectTypeClass);
	}

}
