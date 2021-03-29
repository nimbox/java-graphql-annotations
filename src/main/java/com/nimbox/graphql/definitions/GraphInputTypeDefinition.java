package com.nimbox.graphql.definitions;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;

import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.types.GraphOptionalDefinition;

/**
 * Represents a GraphQL value class that can be of the form
 * {@code Optional<List<Optional<T>>>}. The {@code Optional} and {@code List}
 * classes can be any one of ones available in the registry. It is used to
 * represent the return parameters from fields and the argument parameters from
 * the internal data fetchers.
 *
 */
public class GraphInputTypeDefinition extends GraphWrappedTypeDefinition {

	// properties

	final GraphOptionalDefinition<?> optionalDefinition;
	final GraphOptionalDefinition<?> optionalListDefinition;

	// constructors

	public GraphInputTypeDefinition(final GraphRegistry registry, final AnnotatedElement element, final Type parameterizedType) {
		super(new Builder(registry, element, parameterizedType));

		this.optionalDefinition = registry.getOptionalDefinition(this.optionalType);
		this.optionalListDefinition = registry.getOptionalDefinition(this.optionalListType);

	}

	// methods

	public Object undefined() {
		return optionalDefinition.undefined().get();
	}

	public Object nullable(Object value) {
		return optionalDefinition.nullable().apply(value);
	}

	public Object undefinedList() {
		return optionalListDefinition.undefined().get();
	}

	public Object nullableList(Object value) {
		return optionalListDefinition.nullable().apply(value);
	}

}
