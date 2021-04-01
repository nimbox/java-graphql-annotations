package com.nimbox.graphql.nodes;

import static graphql.schema.GraphQLList.list;
import static graphql.schema.GraphQLNonNull.nonNull;

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

	static GraphOutput of(final GraphRegistry registry, final String name, final GraphOutputTypeDefinition definition) {

		Class<?> type = definition.getType();

		if (registry.getScalars().containsType(type)) {
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

		if (registry.getUnions().contains(type)) {
			return new GraphOutputUnion(definition);
		}

		throw new GraphBuilderException(String.format( //
				"Output Element %s does not have a recognized return type %s", //
				name, type //
		));

	}

	public static GraphOutput of(final GraphRegistry registry, final String name, final Method method) {
		return of(registry, name, new GraphOutputTypeDefinition(registry, method));
	}

	// getters

	public GraphOutputTypeDefinition getDefinition() {
		return definition;
	}

	// builder

	public GraphQLOutputType getGraphQLOutputType(final GraphRegistry registry) {

		GraphQLOutputType type = getInternalGraphQLOutputType(registry);

		if (!definition.isOptional()) {
			type = nonNull(type);
		}

		if (definition.isList()) {
			type = list(type);
			if (!definition.isOptionalList()) {
				type = nonNull(type);
			}
		}

		return type;

	}

	abstract GraphQLOutputType getInternalGraphQLOutputType(final GraphRegistry registry);

}
