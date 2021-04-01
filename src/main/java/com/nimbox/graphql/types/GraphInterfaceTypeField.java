package com.nimbox.graphql.types;

import java.lang.reflect.Method;

import com.nimbox.graphql.annotations.GraphQLField;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeParameterFactory;
import com.nimbox.graphql.runtime.RuntimeSourceDataFetcher;

import graphql.schema.DataFetcher;

public class GraphInterfaceTypeField extends GraphField {

	// constructor

	public GraphInterfaceTypeField(final GraphRegistry registry, Class<?> container, final Method method) {
		super(registry, container, method, registry.getInterfaces().extractTypeFieldData(container, method));
	}

	// methods

	@Override
	public DataFetcher<?> getDataFetcher(RuntimeParameterFactory factory) {
		return new RuntimeSourceDataFetcher<>(factory, container, method, parameters);
	}

	// object overrides

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("@").append(GraphQLField.class.getSimpleName()).append("(").append("name").append(" = ").append(name).append(")");
		builder.append(" ");
		builder.append(container);

		return builder.toString();

	}

}
