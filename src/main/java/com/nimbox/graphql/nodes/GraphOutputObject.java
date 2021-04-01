package com.nimbox.graphql.nodes;

import com.nimbox.graphql.definitions.GraphOutputTypeDefinition;
import com.nimbox.graphql.registries.GraphRegistry;

import graphql.schema.GraphQLOutputType;

public class GraphOutputObject extends GraphOutput {

	// constructors

	GraphOutputObject(final GraphOutputTypeDefinition definition) {
		super(definition);
	}

	// getters

	@Override
	public GraphQLOutputType getInternalGraphQLOutputType(final GraphRegistry registry) {
		return registry.getObjects().getGraphQLType(definition.getType());
	}

}
