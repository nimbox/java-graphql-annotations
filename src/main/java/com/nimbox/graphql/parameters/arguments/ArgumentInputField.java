package com.nimbox.graphql.parameters.arguments;

import com.nimbox.graphql.types.GraphValueClass;

public class ArgumentInputField {

	private final String name;
	private final GraphValueClass returnType;

	public ArgumentInputField(String name, GraphValueClass returnType) {
		this.returnType = returnType;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public GraphValueClass getReturnType() {
		return returnType;
	}

}