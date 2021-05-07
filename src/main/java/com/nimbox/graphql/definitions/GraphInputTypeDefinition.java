package com.nimbox.graphql.definitions;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;

import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.registries.IdCoercing;

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

	final IdCoercing<?> idCoercing;

	// constructors

	public GraphInputTypeDefinition(final GraphRegistry registry, final Method method, final AnnotatedType type) {
		super(new Builder(registry, method, type));

		this.idCoercing = isId() ? registry.getIdCoercing(getType()) : null;

	}

	// methods

	public Object parseId(Object value) {
		return idCoercing.parse(((String) value));
	}

	// methods

	public Object undefined() {
		return getOptionalDefinition().undefined().get();
	}

	public Object nullable(Object value) {
		return getOptionalDefinition().nullable().apply(value);
	}

	public Object undefinedList() {
		return getOptionalListDefinition().undefined().get();
	}

	public Object nullableList(Object value) {
		return getOptionalListDefinition().nullable().apply(value);
	}

}
