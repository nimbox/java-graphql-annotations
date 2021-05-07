package com.nimbox.graphql.definitions;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import com.nimbox.graphql.GraphBuilderException;
import com.nimbox.graphql.registries.GraphRegistry;

public class GraphWrappedTypeDefinition extends GraphTypeDefinition {

	//

	private final boolean isId;
	private final boolean isNotNull;
	private final GraphOptionalDefinition<?> optionalDefinition;

	private final Class<?> listType;
	private final boolean isListNotNull;
	private final GraphOptionalDefinition<?> optionalListDefinition;

	//

	GraphWrappedTypeDefinition(Builder builder) {
		super(builder.type);

		this.isId = builder.isId;
		this.isNotNull = builder.isNotNull;
		this.optionalDefinition = builder.optionalDefinition;

		this.listType = builder.listType;
		this.isListNotNull = builder.isListNotNull;
		this.optionalListDefinition = builder.optionalListDefinition;

	}

	public GraphWrappedTypeDefinition(final GraphRegistry registry, final Method method, final AnnotatedType type) {
		this(new Builder(registry, method, type));
	}

	// getters and setters

	public boolean isId() {
		return isId;
	}

	public boolean isNotNull() {
		return isNotNull;
	}

	public boolean hasOptional() {
		return optionalDefinition != null;
	}

	public GraphOptionalDefinition<?> getOptionalDefinition() {
		return optionalDefinition;
	}

	public boolean isList() {
		return listType != null;
	}

	public Class<?> getListType() {
		return listType;
	}

	public boolean isListNotNull() {
		return isListNotNull;
	}

	public boolean hasOptionalList() {
		return optionalListDefinition != null;
	}

	public GraphOptionalDefinition<?> getOptionalListDefinition() {
		return optionalListDefinition;
	}

	//

	static class Builder {

		final GraphRegistry registry;
		final Method method;

		// properties

		Class<?> type;
		boolean isId;
		boolean isNotNull;
		GraphOptionalDefinition<?> optionalDefinition;

		Class<?> listType = null;
		boolean isListNotNull;
		GraphOptionalDefinition<?> optionalListDefinition;

		// properties

		private AnnotatedType currentType;

		// constructors

		Builder(final GraphRegistry registry, final Method method, final AnnotatedType type) {

			this.registry = registry;
			this.method = method;

			currentType = type;

			if (currentType instanceof AnnotatedParameterizedType) {
				consumeList(consumeOptional());
			} else {
				consumeScalar(null);
			}

		}

		//

		private void consumeCurrent() {

			AnnotatedParameterizedType currentAnnotatedParameterized = (AnnotatedParameterizedType) currentType;
			currentType = currentAnnotatedParameterized.getAnnotatedActualTypeArguments()[0];

		}

		private GraphOptionalDefinition<?> consumeOptional() {

			if (currentType instanceof AnnotatedParameterizedType) {

				ParameterizedType currentParameterized = (ParameterizedType) currentType.getType();
				Class<?> currentClass = (Class<?>) currentParameterized.getRawType();

				GraphOptionalDefinition<?> definition = registry.getOptionalDefinition(currentClass);
				if (definition != null) {
					consumeCurrent();
					return definition;
				}

			}

			return null;

		}

		//

		private void consumeList(final GraphOptionalDefinition<?> optionalClass) {

			if (currentType instanceof AnnotatedParameterizedType) {

				ParameterizedType currentParameterized = (ParameterizedType) currentType.getType();
				Class<?> currentClass = (Class<?>) currentParameterized.getRawType();

				if (List.class.isAssignableFrom(currentClass)) {

					listType = currentClass;
					isListNotNull = registry.isNotNull(method, currentType);
					optionalListDefinition = optionalClass;

					consumeCurrent();
					GraphOptionalDefinition<?> definition = consumeOptional();
					if (!(currentType instanceof AnnotatedParameterizedType)) {
						consumeScalar(definition);
					} else {
						throw new GraphBuilderException("Return type must be of a subset of Optional<List<Optional<T>>>");
					}

				} else {
					throw new GraphBuilderException("Return type must be of a subset of Optional<List<Optional<T>>>");
				}

			} else {
				consumeScalar(optionalClass);
			}

		}

		private void consumeScalar(final GraphOptionalDefinition<?> optionalClass) {

			type = (Class<?>) currentType.getType();
			isId = registry.isId(currentType);
			isNotNull = registry.isNotNull(method, currentType);
			optionalDefinition = optionalClass;

			if (isId && !registry.isIdType((Class<?>) type)) {
				throw new GraphBuilderException(String.format("Unrecognized id type %s", type));
			}

		}

	}

}
