package com.nimbox.graphql.outputs;

import com.nimbox.graphql.definitions.GraphOutputTypeDefinition;
import com.nimbox.graphql.registries.GraphRegistry;

import graphql.schema.GraphQLOutputType;

public class GraphOutputInterface extends GraphOutput {

	// constructors

	public GraphOutputInterface(final GraphOutputTypeDefinition definition) {
		super(definition);
	}

	// getters

	@Override
	public GraphQLOutputType getGraphQLOutputType(GraphRegistry registry) {
		return registry.getInterfaces().getGraphQLType(definition.getType());
	}

}
