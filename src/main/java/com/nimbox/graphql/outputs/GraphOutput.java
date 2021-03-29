package com.nimbox.graphql.outputs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.nimbox.graphql.GraphBuilderException;
import com.nimbox.graphql.definitions.GraphOutputTypeDefinition;
import com.nimbox.graphql.registries.GraphRegistry;

import graphql.schema.GraphQLOutputType;

public abstract class GraphOutput {

	// properties

	final GraphOutputTypeDefinition definition;

	// constructors

	GraphOutput(final GraphOutputTypeDefinition definition) {
		this.definition = definition;
	}

	static GraphOutput of(final GraphRegistry registry, final Class<? extends Annotation> annotation, final String name, final GraphOutputTypeDefinition definition) {

		Class<?> type = definition.getType();

		if (registry.getScalars().contains(type)) {
			return new GraphOutputScalar(definition);
		}

		if (registry.getEnums().contains(type)) {
			return new GraphOutputEnum(definition);
		}

		if (registry.getObjects().contains(type)) {
			return new GraphOutputObject(definition);
		}

		if (registry.getInterfaces().contains(type)) {
			return new GraphOutputInterface(definition);
		}

		throw new GraphBuilderException(String.format( //
				"Element %s annotated with %s does not have a recognized return type %s", //
				name, annotation, type//
		));

	}

	public static GraphOutput of(final GraphRegistry registry, final Class<? extends Annotation> annotation, final String name, final Method method) {
		return of(registry, annotation, name, new GraphOutputTypeDefinition(registry, method));
	}
	// getters

	public GraphOutputTypeDefinition getOutputType() {
		return definition;
	}

	public abstract GraphQLOutputType getGraphQLOutputType(final GraphRegistry registry);

}
