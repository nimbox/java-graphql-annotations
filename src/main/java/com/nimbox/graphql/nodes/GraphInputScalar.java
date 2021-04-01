package com.nimbox.graphql.nodes;

import static graphql.Scalars.GraphQLID;

import com.nimbox.graphql.definitions.GraphInputTypeDefinition;
import com.nimbox.graphql.registries.GraphRegistry;

import graphql.schema.GraphQLInputType;

public class GraphInputScalar extends GraphInput {

	// constructors

	GraphInputScalar(final GraphInputTypeDefinition inputValue) {
		super(inputValue);
	}

	// getters

	@Override
	GraphQLInputType getInternalGraphQLInputType(final GraphRegistry registry) {

		if (definition.isId()) {
			return GraphQLID;
		} else {
			return registry.getScalars().getGraphQLTypeType(definition.getType());
		}

	}

}
