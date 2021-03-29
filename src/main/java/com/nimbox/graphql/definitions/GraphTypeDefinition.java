package com.nimbox.graphql.definitions;

public abstract class GraphTypeDefinition {

	// properties

	final Class<?> type;

	// constructors

	GraphTypeDefinition(Class<?> type) {
		this.type = type;
	}

	// getters

	public Class<?> getType() {
		return type;
	}

}
