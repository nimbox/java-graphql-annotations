package com.nimbox.graphql.inputs;

import static graphql.schema.GraphQLList.list;
import static graphql.schema.GraphQLNonNull.nonNull;

import java.lang.annotation.Annotation;
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

	GraphInput(GraphInputTypeDefinition definition) {
		this.definition = definition;
	}

	static GraphInput of(final GraphRegistry registry, final Class<? extends Annotation> annotation, final String name, final GraphInputTypeDefinition definition) {

		Class<?> type = definition.getType();
		
		if (registry.getScalars().contains(type)) {
			return new GraphInputScalar(definition);
		}

		if (registry.getEnums().contains(type)) {
			return new GraphInputEnum(definition);
		}

		if (registry.getInputObjects().contains(type)) {
			return new GraphInputInputObject(definition);
		}

		throw new GraphBuilderException(String.format( //
				"Element %s annotated with %s does not have a recognized return type %s", //
				name, annotation, type //
		));

	}

	public static GraphInput of(final GraphRegistry registry, final Class<? extends Annotation> annotation, final String name, final Method method) {
		return of(registry, annotation, name, new GraphInputTypeDefinition(registry, method, method.getGenericReturnType()));
	}

	public static GraphInput of(final GraphRegistry registry, final Class<? extends Annotation> annotation, final String name, final Parameter parameter) {
		return of(registry, annotation, name, new GraphInputTypeDefinition(registry, parameter, parameter.getParameterizedType()));
	}

	// getters

	public GraphInputTypeDefinition getDefinition() {
		return definition;
	}

	// builder

	public GraphQLInputType getGraphQLInputType(final GraphRegistry registry) {

		GraphQLInputType type = getInternalGraphQLInputType(registry);

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

	abstract GraphQLInputType getInternalGraphQLInputType(GraphRegistry registry);

}
