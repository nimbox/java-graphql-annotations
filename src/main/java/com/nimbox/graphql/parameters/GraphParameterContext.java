package com.nimbox.graphql.parameters;

import java.lang.reflect.Parameter;

import com.nimbox.graphql.GeneratorException;
import com.nimbox.graphql.annotations.GraphQLContext;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeParameter;
import com.nimbox.graphql.types.GraphValueClass;

import graphql.schema.DataFetchingEnvironment;

public class GraphParameterContext extends GraphParameter {

	// constructors

	public GraphParameterContext(final GraphRegistry registry, final Parameter parameter) {
		super(registry, parameter, new GraphValueClass(registry, parameter, parameter.getParameterizedType()), GraphQLContext.class);

		if (valueClass.isOptional() || valueClass.isList() || !registry.getContext().isAssignableFrom(this.valueClass.getValueClass())) {
			throw new GeneratorException(String.format("Parameter %s annotated with %s must have type %s", //
					parameter.getName(), //
					GraphQLContext.class, //
					registry.getContext()) //
			);
		}

	}

	// getters

	public <T> T getParameterValue(DataFetchingEnvironment environment) {
		return environment.getContext();
	}

	@Override
	public RuntimeParameter getRuntimeParameter() {
		return new RuntimeParameter(valueClass);
	}

	// object overrides

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("@").append(GraphQLContext.class);
		builder.append(" ");
		builder.append(valueClass.getValueClass());

		return builder.toString();

	}

}
