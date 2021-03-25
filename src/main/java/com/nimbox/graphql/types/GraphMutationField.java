package com.nimbox.graphql.types;

import static com.nimbox.graphql.utils.IntrospectionUtils.getAnnotationOrThrow;

import java.lang.reflect.Method;

import com.nimbox.graphql.annotations.GraphQLMutation;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeInstanceDataFetcher;
import com.nimbox.graphql.runtime.RuntimeParameterFactory;
import com.nimbox.graphql.utils.ReservedStrings;

import graphql.schema.DataFetcher;

public class GraphMutationField extends GraphField {

	// constructor

	public GraphMutationField(final GraphRegistry registry, Class<?> typeClass, final Method fieldMethod) {
		super(registry, new MutationDefinition(fieldMethod), typeClass, fieldMethod);
	}

	// methods

	@Override
	public DataFetcher<?> getFetcher(RuntimeParameterFactory factory) {
		return new RuntimeInstanceDataFetcher<>(factory, typeClass, typeMethod, parameters);
	}

	// object overrides

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("@").append(GraphQLMutation.class.getSimpleName()).append("(").append("name").append(" = ").append(name).append(")");
		builder.append(" ");
		builder.append(valueClass);

		return builder.toString();

	}

	// classes

	public static class MutationDefinition implements Definition {

		private final GraphQLMutation annotation;

		public MutationDefinition(final Method fieldMethod) {
			annotation = getAnnotationOrThrow(GraphQLMutation.class, fieldMethod);
		}

		@Override
		public String getName() {
			return annotation.name();
		}

		@Override
		public String getDescription() {
			return ReservedStrings.translate(annotation.description());
		}

		@Override
		public String getDeprecationReason() {
			return ReservedStrings.translate(annotation.deprecationReason());
		}

	}

}
