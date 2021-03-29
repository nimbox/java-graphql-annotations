package com.nimbox.graphql.inputs;

import static graphql.Scalars.GraphQLID;

import com.nimbox.graphql.definitions.GraphInputTypeDefinition;
import com.nimbox.graphql.registries.GraphRegistry;

import graphql.schema.GraphQLInputType;

public class GraphInputScalar extends GraphInput {

	// constructors

	GraphInputScalar(GraphInputTypeDefinition inputValue) {
		super(inputValue);
	}

	// getters

	@Override
	GraphQLInputType getInternalGraphQLInputType(GraphRegistry registry) {

		if (definition.isId()) {
			return GraphQLID;
		} else {
			return registry.getScalars().getGraphQLType(definition.getType());
		}

	}

}
