package com.nimbox.graphql.nodes;

import static graphql.Scalars.GraphQLID;

import com.nimbox.graphql.definitions.GraphOutputTypeDefinition;
import com.nimbox.graphql.registries.GraphRegistry;

import graphql.schema.GraphQLOutputType;

public class GraphOutputScalar extends GraphOutput {

	// constructors

	GraphOutputScalar(final GraphOutputTypeDefinition definition) {
		super(definition);
	}

	// getters

	@Override
	public GraphQLOutputType getInternalGraphQLOutputType(final GraphRegistry registry) {

		if (definition.isId()) {
			return GraphQLID;
		} else {
			return registry.getScalars().getGraphQLTypeType(definition.getType());
		}

	}

}
