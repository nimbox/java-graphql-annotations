package com.nimbox.graphql.runtime;

import com.nimbox.graphql.types.GraphValueClass;

public class RuntimeParameter {

	// properties

	protected final GraphValueClass type;
	protected final String name;

	// constructors

	public RuntimeParameter(GraphValueClass type) {
		this.type = type;
		this.name = null;
	}

	public RuntimeParameter(GraphValueClass type, String name) {
		this.type = type;
		this.name = name;
	}

}
