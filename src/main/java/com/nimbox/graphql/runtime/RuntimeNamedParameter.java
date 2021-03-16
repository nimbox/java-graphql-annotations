package com.nimbox.graphql.runtime;

import com.nimbox.graphql.types.GraphValueClass;

public class RuntimeNamedParameter extends RuntimeParameter {

	// properties

	protected final String name;

	// constructors

	public RuntimeNamedParameter(final GraphValueClass valueClass, final String name) {
		super(valueClass);
		this.name = name;
	}

}
