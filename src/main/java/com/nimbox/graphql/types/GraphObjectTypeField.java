package com.nimbox.graphql.types;

import java.lang.reflect.Method;

import com.nimbox.graphql.annotations.GraphQLField;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.registries.GraphRegistry.FieldAnnotation;
import com.nimbox.graphql.runtime.RuntimeParameterFactory;
import com.nimbox.graphql.runtime.RuntimeSourceDataFetcher;

import graphql.schema.DataFetcher;

public class GraphObjectTypeField extends GraphField {

	// constructor

	public GraphObjectTypeField(final GraphRegistry registry, Class<?> container, final Method method) {
		super(registry, new ObjetTypeAnnotation(registry, method), container, method);
	}

	// methods

	@Override
	public DataFetcher<?> getFetcher(RuntimeParameterFactory factory) {
		return new RuntimeSourceDataFetcher<>(factory, container, method, parameters);
	}

	// object overrides

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("@").append(GraphQLField.class.getSimpleName()).append("(").append("name").append(" = ").append(name).append(")");
		builder.append(" ");
		builder.append(container);

		return builder.toString();

	}

	// classes

	public static class ObjetTypeAnnotation implements TypeAnnotation {

		private final FieldAnnotation<?>.Content annotation;

		public ObjetTypeAnnotation(final GraphRegistry registry, final Method method) {
			annotation = registry.getFieldAnnotationOrThrow(method);
		}

		@Override
		public String getName() {
			return annotation.getName();
		}

		@Override
		public String getDescription() {
			return annotation.getDescription();
		}

		@Override
		public String getDeprecationReason() {
			return annotation.getDeprecationReason();
		}

	}

}
