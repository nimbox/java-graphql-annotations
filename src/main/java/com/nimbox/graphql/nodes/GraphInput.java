package com.nimbox.graphql.nodes;

import static graphql.schema.GraphQLList.list;
import static graphql.schema.GraphQLNonNull.nonNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import com.nimbox.graphql.GraphBuilderException;
import com.nimbox.graphql.definitions.GraphInputTypeDefinition;
import com.nimbox.graphql.registries.GraphRegistry;

import graphql.schema.GraphQLInputType;

public abstract class GraphInput {

	// properties

	final GraphInputTypeDefinition definition;

	// constructors

	GraphInput(final GraphInputTypeDefinition definition) {
		this.definition = definition;
	}

	static GraphInput of(final GraphRegistry registry, final String name, final GraphInputTypeDefinition definition) {

		Class<?> type = definition.getType();

		if (registry.getScalars().containsType(type)) {
			return new GraphInputScalar(definition);
		}

		if (registry.getEnums().contains(type)) {
			return new GraphInputEnum(definition);
		}

		if (registry.getInputObjects().contains(type)) {
			return new GraphInputInputObject(definition);
		}

		throw new GraphBuilderException(String.format( //
				"Input Element %s does not have a recognized return type %s", //
				name, type //
		));

	}

	public static GraphInput of(final GraphRegistry registry, final String name, final Method method) {
		return of(registry, name, new GraphInputTypeDefinition(registry, method, method.getAnnotatedReturnType()));
	}

	public static GraphInput of(final GraphRegistry registry, final String name, final Method method, final Parameter parameter) {
		return of(registry, name, new GraphInputTypeDefinition(registry, method, parameter.getAnnotatedType()));
	}

	// getters

	public GraphInputTypeDefinition getDefinition() {
		return definition;
	}

	// builder

	public GraphQLInputType getGraphQLInputType(final GraphRegistry registry) {

		GraphQLInputType type = getInternalGraphQLInputType(registry);

		if (definition.isNotNull()) {
			type = nonNull(type);
		}

		if (definition.isList()) {
			type = list(type);
			if (definition.isListNotNull()) {
				type = nonNull(type);
			}
		}

		return type;

	}

	abstract GraphQLInputType getInternalGraphQLInputType(final GraphRegistry registry);

}
