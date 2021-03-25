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

	public GraphObjectTypeField(final GraphRegistry registry, Class<?> typeClass, final Method fieldMethod) {
		super(registry, new ObjetTypeDefinition(registry, fieldMethod), typeClass, fieldMethod);
	}

	// methods

	@Override
	public DataFetcher<?> getFetcher(RuntimeParameterFactory factory) {
		return new RuntimeSourceDataFetcher<>(factory, typeClass, typeMethod, parameters);
	}

	// object overrides

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("@").append(GraphQLField.class.getSimpleName()).append("(").append("name").append(" = ").append(name).append(")");
		builder.append(" ");
		builder.append(valueClass);

		return builder.toString();

	}

	// classes

	public static class ObjetTypeDefinition implements Definition {

		private final FieldAnnotation<?>.Content annotation;

		public ObjetTypeDefinition(final GraphRegistry registry, final Method fieldMethod) {
			annotation = registry.getFieldAnnotationOrThrow(fieldMethod);
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
