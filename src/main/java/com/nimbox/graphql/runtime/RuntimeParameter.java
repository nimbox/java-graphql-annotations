package com.nimbox.graphql.runtime;

import com.nimbox.graphql.definitions.GraphTypeDefinition;

public class RuntimeParameter {

	// properties

	final String name;
	final GraphTypeDefinition definition;

	// constructors

	public RuntimeParameter(GraphTypeDefinition type) {
		this.name = null;
		this.definition = type;
	}

	public RuntimeParameter(String name, GraphTypeDefinition type) {
		this.definition = type;
		this.name = name;
	}

	// getters

	public String getName() {
		return name;
	}

	public GraphTypeDefinition getDefinition() {
		return definition;
	}

}
