package com.nimbox.graphql.definitions;

public class GraphContextTypeDefinition extends GraphTypeDefinition {

	// constructors

	public GraphContextTypeDefinition(Class<?> type) {
		super(type);
	}

	// object overrides

	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append(type.getTypeName());

		return builder.toString();

	}

}
