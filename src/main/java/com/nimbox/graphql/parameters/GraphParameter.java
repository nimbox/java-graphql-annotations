package com.nimbox.graphql.parameters;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import com.nimbox.graphql.GraphBuilderException;
import com.nimbox.graphql.annotations.GraphQLArgument;
import com.nimbox.graphql.annotations.GraphQLContext;
import com.nimbox.graphql.definitions.GraphInputTypeDefinition;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeParameter;

public abstract class GraphParameter {

	// constructors

	GraphParameter() {
	}

	public static GraphParameter of(final GraphRegistry registry, final Method method, final Parameter parameter) {

		if (parameter.isAnnotationPresent(GraphQLArgument.class)) {
			return new GraphParameterArgument(registry, parameter);
		}

		if (parameter.isAnnotationPresent(GraphQLContext.class)) {
			return new GraphParameterContext(registry, parameter);
		}

		throw new GraphBuilderException(String.format( //
				"Parameter %s of type %s in method %s does not have a recognized annotation", //
				parameter.getName(), parameter.getType(), method //
		));

	}

	// getters

	public abstract GraphInputTypeDefinition getDefinition();

	public abstract RuntimeParameter getRuntimeParameter();

}
