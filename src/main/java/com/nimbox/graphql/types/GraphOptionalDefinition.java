package com.nimbox.graphql.types;

import java.util.function.Function;
import java.util.function.Supplier;

public class GraphOptionalDefinition<U> {

	// properties

	private final Class<U> klass;
	private final Supplier<U> undefined;
	private final Function<Object, U> nullable;

	// constructors

	public GraphOptionalDefinition(Class<U> klass, Supplier<U> undefined, Function<Object, U> nullable) {
		super();
		this.klass = klass;
		this.undefined = undefined;
		this.nullable = nullable;
	}

	// getters

	public Class<U> getKlass() {
		return klass;
	}

	public Supplier<U> undefined() {
		return undefined;
	}

	public Function<Object, U> nullable() {
		return nullable;
	}

}
