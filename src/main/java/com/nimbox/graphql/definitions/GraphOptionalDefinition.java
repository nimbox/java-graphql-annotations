package com.nimbox.graphql.definitions;

import java.util.function.Function;
import java.util.function.Supplier;

public class GraphOptionalDefinition<U> {

	// properties

	private final Class<U> container;
	private final Supplier<U> undefined;
	private final Function<Object, U> nullable;

	// constructors

	public GraphOptionalDefinition(Class<U> container, Supplier<U> undefined, Function<Object, U> nullable) {
		super();
		this.container = container;
		this.undefined = undefined;
		this.nullable = nullable;
	}

	// getters

	public Class<U> getContainer() {
		return container;
	}

	public Supplier<U> undefined() {
		return undefined;
	}

	public Function<Object, U> nullable() {
		return nullable;
	}

}
