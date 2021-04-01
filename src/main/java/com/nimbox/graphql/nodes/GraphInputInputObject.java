package com.nimbox.graphql.nodes;

import com.nimbox.graphql.definitions.GraphInputTypeDefinition;
import com.nimbox.graphql.registries.GraphRegistry;

import graphql.schema.GraphQLInputType;

public class GraphInputInputObject extends GraphInput {

	// constructors

	public GraphInputInputObject(final GraphInputTypeDefinition inputValue) {
		super(inputValue);
	}

	// getters

	@Override
	GraphQLInputType getInternalGraphQLInputType(final GraphRegistry registry) {
		return registry.getInputObjects().getGraphQLType(definition.getType());
	}

}
