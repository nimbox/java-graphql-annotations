package com.nimbox.graphql.runtime;

import com.nimbox.graphql.types.GraphValueClass;

public class RuntimeParameter {

	// properties

	protected final String name;
	protected final GraphValueClass valueClass;

	// constructors

	public RuntimeParameter(GraphValueClass valueClass) {
		this.valueClass = valueClass;
		this.name = null;
	}

	public RuntimeParameter(String name, GraphValueClass valueClass) {
		this.name = name;
		this.valueClass = valueClass;
	}

}
