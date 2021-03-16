package com.nimbox.graphql.types;

import static graphql.Scalars.GraphQLID;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.nimbox.graphql.GeneratorException;
import com.nimbox.graphql.annotations.GraphQLId;
import com.nimbox.graphql.registries.GraphRegistry;

import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLOutputType;

/**
 * Represents a GraphQL value class that can be of the form
 * {@code Optional<List<Optional<T>>>}. The {@code Optional} and {@code List}
 * classes can be any one of ones available in the registry. It is used to
 * represent the return parameters from fields and the argument parameters from
 * the internal data fetchers.
 *
 */
public class GraphValueClass {

	// properties

	private final Class<?> valueClass;
	private final boolean isId;

	private final Class<?> optionalClass;
	private final GraphOptionalDefinition<?> optionalDefinition;

	private final Class<?> listClass;
	private final Class<?> optionalListClass;
	private final GraphOptionalDefinition<?> optionalListDefinition;

	// constructors

	public GraphValueClass(final Type valueClass) {

		this.valueClass = (Class<?>) valueClass;
		this.isId = false;

		this.optionalClass = null;
		this.optionalDefinition = null;

		this.listClass = null;
		this.optionalListClass = null;
		this.optionalListDefinition = null;

	}

	public GraphValueClass(final GraphRegistry registry, final AnnotatedElement element, final Type typeClass) {

		Builder builder = new Builder(registry, element, typeClass);

		this.isId = builder.isId;

		this.valueClass = builder.valueClass;
		this.optionalClass = builder.optionalClass;
		this.optionalDefinition = registry.getOptionalDefinition(builder.optionalClass);

		this.listClass = builder.listClass;
		this.optionalListClass = builder.optionalListClass;
		this.optionalListDefinition = registry.getOptionalDefinition(builder.optionalListClass);

	}

	// getters

	public Class<?> getValueClass() {
		return valueClass;
	}

	public boolean isId() {
		return isId;
	}

	public boolean isOptional() {
		return optionalClass != null;
	}

	public boolean isList() {
		return listClass != null;
	}

	public boolean isOptionalList() {
		return optionalListClass != null;
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

	public GraphQLOutputType getGraphQLOutputValueType(GraphRegistry registry) {

		GraphQLOutputType type = isId ? GraphQLID : registry.getOutputType(valueClass);

		if (!isOptional()) {
			type = GraphQLNonNull.nonNull(type);
		}

		if (isList()) {
			type = GraphQLList.list(type);
			if (!isOptionalList()) {
				type = GraphQLNonNull.nonNull(type);
			}
		}

		return type;

	}

	public GraphQLInputType getGraphQLInputValueType(GraphRegistry registry) {

		GraphQLInputType type = isId ? GraphQLID : registry.getInputType(valueClass);

		if (!isOptional()) {
			type = GraphQLNonNull.nonNull(type);
		}

		if (isList()) {
			type = GraphQLList.list(type);
			if (!isOptionalList()) {
				type = GraphQLNonNull.nonNull(type);
			}
		}

		return type;

	}

	// object overrides

	public String toString() {

		StringBuilder builder = new StringBuilder();

		List<String> types = new ArrayList<String>();

		if (isOptionalList()) {
			types.add("Optional");
		}
		if (isList()) {
			types.add("List");
		}
		if (isOptional()) {
			types.add("Optional");
		}
		types.add(valueClass.getTypeName());

		return toString(builder, types).toString();

	}

	private StringBuilder toString(StringBuilder builder, List<String> types) {

		if (types.isEmpty()) {
			return builder;
		}

		if (types.size() == 1) {
			builder.append(types.get(0));
			return builder;
		}

		builder.append(types.get(0)).append("<");
		toString(builder, types.subList(1, types.size()));
		builder.append(">");

		return builder;

	}

	// builder

	private static class Builder {

		private boolean isId;

		private Class<?> valueClass;
		private Class<?> optionalClass = null;

		private Class<?> listClass = null;
		private Class<?> optionalListClass = null;

		public Builder(final GraphRegistry registry, final AnnotatedElement element, final Type type) {

			this.isId = element.isAnnotationPresent(GraphQLId.class);

			if (type instanceof ParameterizedType) {
				checkOptionalOrList(registry, type);
			} else {
				complete(type);
			}

		}

		private void checkOptionalOrList(final GraphRegistry registry, final Type type) {

			ParameterizedType parameterized = (ParameterizedType) type;

			if (registry.isOptional((Class<?>) parameterized.getRawType())) {

				Type current = parameterized.getActualTypeArguments()[0];
				if (current instanceof ParameterizedType) {
					optionalListClass = (Class<?>) parameterized.getRawType();
					checkList(registry, current);
				} else {
					optionalClass = (Class<?>) parameterized.getRawType();
					complete(current);
				}

				return;

			}

			if (registry.isList((Class<?>) parameterized.getRawType())) {

				listClass = (Class<?>) parameterized.getRawType();

				Type current = parameterized.getActualTypeArguments()[0];
				if (current instanceof ParameterizedType) {
					checkOptional(registry, current);
				} else {
					complete(current);
				}

				return;

			}

			throw new GeneratorException("Return type must be of a subset of Optional<List<Optional<T>>>");

		}

		private void checkList(final GraphRegistry registry, final Type type) {

			ParameterizedType parameterized = (ParameterizedType) type;

			if (registry.isList((Class<?>) parameterized.getRawType())) {

				listClass = (Class<?>) parameterized.getRawType();

				Type current = parameterized.getActualTypeArguments()[0];
				if (current instanceof ParameterizedType) {
					checkOptional(registry, current);
				} else {
					complete(current);
				}

				return;

			}

			throw new GeneratorException("Return type must be of a subset of Optional<List<Optional<T>>>");

		}

		private void checkOptional(final GraphRegistry registry, final Type type) {

			ParameterizedType parameterized = (ParameterizedType) type;

			if (registry.isOptional((Class<?>) parameterized.getRawType())) {

				optionalClass = (Class<?>) parameterized.getRawType();

				Type current = parameterized.getActualTypeArguments()[0];
				if (!(current instanceof ParameterizedType)) {
					complete(current);
					return;
				}

			}

			throw new GeneratorException("Return type must be of a subset of Optional<List<Optional<T>>>");

		}

		private void complete(final Type type) {

			if (type.equals(Void.TYPE)) {
				throw new GeneratorException("Return type must not be void");
			}
			this.valueClass = (Class<?>) type;

		}

	}

}
