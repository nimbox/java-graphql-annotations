package com.nimbox.graphql.inputs;

import com.nimbox.graphql.definitions.GraphInputTypeDefinition;
import com.nimbox.graphql.registries.GraphRegistry;

import graphql.schema.GraphQLInputType;

public class GraphInputEnum extends GraphInput {

	// constructors

	GraphInputEnum(GraphInputTypeDefinition inputValue) {
		super(inputValue);
	}

	// getters

	@Override
	GraphQLInputType getInternalGraphQLInputType(GraphRegistry registry) {
		return registry.getEnums().getGraphQLType(definition.getType());
	}

}
