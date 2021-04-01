package com.nimbox.graphql.nodes;

import com.nimbox.graphql.definitions.GraphOutputTypeDefinition;
import com.nimbox.graphql.registries.GraphRegistry;

import graphql.schema.GraphQLOutputType;

public class GraphOutputUnion extends GraphOutput {

	// constructors

	GraphOutputUnion(final GraphOutputTypeDefinition definition) {
		super(definition);
	}

	// getters

	@Override
	public GraphQLOutputType getInternalGraphQLOutputType(final GraphRegistry registry) {
		return registry.getUnions().getGraphQLType(definition.getType());
	}

}
