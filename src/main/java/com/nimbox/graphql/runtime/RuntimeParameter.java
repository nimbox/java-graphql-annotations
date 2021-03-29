package com.nimbox.graphql.runtime;

import com.nimbox.graphql.definitions.GraphInputTypeDefinition;

public class RuntimeParameter {

	// properties

	protected final GraphInputTypeDefinition type;
	protected final String name;

	// constructors

	public RuntimeParameter(GraphInputTypeDefinition type) {
		this.type = type;
		this.name = null;
	}

	public RuntimeParameter(GraphInputTypeDefinition type, String name) {
		this.type = type;
		this.name = name;
	}

}
