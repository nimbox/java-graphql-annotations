package com.nimbox.graphql.types;

import java.lang.reflect.Method;

import com.nimbox.graphql.annotations.GraphQLQuery;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeInstanceDataFetcher;
import com.nimbox.graphql.runtime.RuntimeParameterFactory;

import graphql.schema.DataFetcher;

public class GraphQueryField extends GraphField {

	// constructor

	public GraphQueryField(final GraphRegistry registry, Class<?> container, final Method method) {
		super(registry, container, method, registry.getObjects().extractTypeFieldData(container, method));
	}

	// methods

	@Override
	public DataFetcher<?> getDataFetcher(RuntimeParameterFactory factory) {
		return new RuntimeInstanceDataFetcher<>(factory, container, method, parameters);
	}

	// object overrides

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("@").append(GraphQLQuery.class.getSimpleName()).append("(").append("name").append(" = ").append(name).append(")");
		builder.append(" ");
		builder.append(container);

		return builder.toString();

	}

}
