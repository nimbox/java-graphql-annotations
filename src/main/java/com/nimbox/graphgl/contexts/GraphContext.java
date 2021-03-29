package com.nimbox.graphgl.contexts;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

import com.nimbox.graphql.GraphBuilderException;
import com.nimbox.graphql.definitions.GraphContextTypeDefinition;
import com.nimbox.graphql.registries.GraphRegistry;

public class GraphContext {

	// properties

	final GraphContextTypeDefinition definition;

	// constructors

	GraphContext(GraphContextTypeDefinition definition) {
		this.definition = definition;
	}

	public static GraphContext of(final GraphRegistry registry, final Class<? extends Annotation> annotation, final String name, final Parameter parameter) {

		GraphContextTypeDefinition definition = new GraphContextTypeDefinition(parameter.getType());
		Class<?> type = definition.getType();

		if (registry.getContexts().containsKey(type)) {
			return new GraphContext(definition);
		}

		throw new GraphBuilderException(String.format( //
				"Element %s annotated with %s does not have a recognized return type %s", //
				name, annotation, type //
		));

	}

	// getters

	public GraphContextTypeDefinition getDefinition() {
		return definition;
	}

}
