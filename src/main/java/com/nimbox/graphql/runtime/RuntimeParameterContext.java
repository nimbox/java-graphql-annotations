package com.nimbox.graphql.runtime;

import com.nimbox.graphql.definitions.GraphTypeDefinition;

public class RuntimeParameterContext extends RuntimeParameter {

	// constructors

	public RuntimeParameterContext(GraphTypeDefinition type) {
		super(type);
	}

	public RuntimeParameterContext(String name, GraphTypeDefinition type) {
		super(name, type);
	}

}
