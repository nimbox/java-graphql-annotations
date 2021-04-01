package com.nimbox.graphql.runtime;

import com.nimbox.graphql.definitions.GraphInputTypeDefinition;

public class RuntimeParameterArgument extends RuntimeParameter {

	// constructors

	public RuntimeParameterArgument(GraphInputTypeDefinition type) {
		super(type);
	}

	public RuntimeParameterArgument(String name, GraphInputTypeDefinition type) {
		super(name, type);
	}

	// getters

	@Override
	public GraphInputTypeDefinition getDefinition() {
		return (GraphInputTypeDefinition) definition;
	}

}
