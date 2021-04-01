package com.nimbox.graphql.definitions;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.nimbox.graphql.GraphBuilderException;
import com.nimbox.graphql.registries.GraphRegistry;

public class GraphWrappedTypeDefinition extends GraphTypeDefinition {

	// properties

	final boolean isId;

	final Class<?> optionalType;
	final Class<?> listType;
	final Class<?> optionalListType;

	// constructors

	GraphWrappedTypeDefinition(Builder builder) {
		super(builder.type);

		this.isId = builder.isId;

		this.optionalType = builder.optionalType;
		this.listType = builder.listType;
		this.optionalListType = builder.optionalListType;

	}

	// getters

	public boolean isId() {
		return isId;
	}

	public boolean isOptional() {
		return optionalType != null;
	}

	public boolean isList() {
		return listType != null;
	}

	public boolean isOptionalList() {
		return optionalListType != null;
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
		types.add(type.getTypeName());

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

	static class Builder {

		Class<?> type;
		boolean isId;
		boolean isNotNull;

		Class<?> optionalType = null;

		Class<?> listType = null;
		Class<?> optionalListType = null;

		Builder(final GraphRegistry registry, final AnnotatedElement element, final Type type) {

			this.isId = registry.isId(element);
			this.isNotNull = registry.isNotNull(element);

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
					optionalListType = (Class<?>) parameterized.getRawType();
					checkList(registry, current);
				} else {
					optionalType = (Class<?>) parameterized.getRawType();
					complete(current);
				}

				return;

			}

			if (registry.isList((Class<?>) parameterized.getRawType())) {

				listType = (Class<?>) parameterized.getRawType();

				Type current = parameterized.getActualTypeArguments()[0];
				if (current instanceof ParameterizedType) {
					checkOptional(registry, current);
				} else {
					complete(current);
				}

				return;

			}

			throw new GraphBuilderException("Return type must be of a subset of Optional<List<Optional<T>>>");

		}

		private void checkList(final GraphRegistry registry, final Type type) {

			ParameterizedType parameterized = (ParameterizedType) type;

			if (registry.isList((Class<?>) parameterized.getRawType())) {

				listType = (Class<?>) parameterized.getRawType();

				Type current = parameterized.getActualTypeArguments()[0];
				if (current instanceof ParameterizedType) {
					checkOptional(registry, current);
				} else {
					complete(current);
				}

				return;

			}

			throw new GraphBuilderException("Return type must be of a subset of Optional<List<Optional<T>>>");

		}

		private void checkOptional(final GraphRegistry registry, final Type type) {

			ParameterizedType parameterized = (ParameterizedType) type;

			if (registry.isOptional((Class<?>) parameterized.getRawType())) {

				optionalType = (Class<?>) parameterized.getRawType();

				Type current = parameterized.getActualTypeArguments()[0];
				if (!(current instanceof ParameterizedType)) {
					complete(current);
					return;
				}

			}

			throw new GraphBuilderException("Return type must be of a subset of Optional<List<Optional<T>>>");

		}

		private void complete(final Type type) {

			if (type.equals(Void.TYPE)) {
				throw new GraphBuilderException("Return type must not be void");
			}
			this.type = (Class<?>) type;

		}

	}

}
