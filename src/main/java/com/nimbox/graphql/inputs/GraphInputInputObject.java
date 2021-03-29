package com.nimbox.graphql.inputs;

import com.nimbox.graphql.definitions.GraphInputTypeDefinition;
import com.nimbox.graphql.registries.GraphRegistry;

import graphql.schema.GraphQLInputType;

public class GraphInputInputObject extends GraphInput {

	// constructors

	public GraphInputInputObject(GraphInputTypeDefinition inputValue) {
		super(inputValue);
	}

	// getters

	@Override
	GraphQLInputType getInternalGraphQLInputType(GraphRegistry registry) {
		return registry.getInputObjects().getGraphQLType(definition.getType());
	}

}
