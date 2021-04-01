package com.nimbox.graphql.nodes;

import com.nimbox.graphql.definitions.GraphOutputTypeDefinition;
import com.nimbox.graphql.registries.GraphRegistry;

import graphql.schema.GraphQLOutputType;

public class GraphOutputEnum extends GraphOutput {

	// constructors

	GraphOutputEnum(final GraphOutputTypeDefinition definition) {
		super(definition);
	}

	// getters

	@Override
	public GraphQLOutputType getInternalGraphQLOutputType(final GraphRegistry registry) {
		return registry.getEnums().getGraphQLType(definition.getType());
	}

}
