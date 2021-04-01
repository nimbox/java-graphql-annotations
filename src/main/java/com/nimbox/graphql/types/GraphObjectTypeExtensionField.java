package com.nimbox.graphql.types;

import java.lang.reflect.Method;

import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeParameterFactory;
import com.nimbox.graphql.runtime.RuntimeSourceExtensionDataFetcher;

import graphql.schema.DataFetcher;

public class GraphObjectTypeExtensionField extends GraphObjectTypeField {

	// properties

	private Class<?> referenceContainer;

	// constructor

	public GraphObjectTypeExtensionField(final GraphRegistry registry, final Class<?> container, final Method method, final Class<?> referenceContainer) {
		super(registry, container, method);
		this.referenceContainer = referenceContainer;
	}

	// methods

	@Override
	public DataFetcher<?> getDataFetcher(RuntimeParameterFactory factory) {
		return new RuntimeSourceExtensionDataFetcher<>(factory, container, method, parameters, referenceContainer);
	}

}
