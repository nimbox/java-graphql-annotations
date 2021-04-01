package com.nimbox.graphql.nodes;

import com.nimbox.graphql.definitions.GraphInputTypeDefinition;
import com.nimbox.graphql.registries.GraphRegistry;

import graphql.schema.GraphQLInputType;

public class GraphInputEnum extends GraphInput {

	// constructors

	GraphInputEnum(final GraphInputTypeDefinition inputValue) {
		super(inputValue);
	}

	// getters

	@Override
	GraphQLInputType getInternalGraphQLInputType(final GraphRegistry registry) {
		return registry.getEnums().getGraphQLType(definition.getType());
	}

}
