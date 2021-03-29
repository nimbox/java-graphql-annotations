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

	public GraphMutationField(final GraphRegistry registry, Class<?> container, final Method method) {
		super(registry, new MutationAnnotation(method), container, method);
	}

	// methods

	@Override
	public DataFetcher<?> getFetcher(RuntimeParameterFactory factory) {
		return new RuntimeInstanceDataFetcher<>(factory, container, method, parameters);
	}

	// object overrides

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("@").append(GraphQLMutation.class.getSimpleName()).append("(").append("name").append(" = ").append(name).append(")");
		builder.append(" ");
		builder.append(container);

		return builder.toString();

	}

	// classes

	public static class MutationAnnotation implements TypeAnnotation {

		private final GraphQLMutation annotation;

		public MutationAnnotation(final Method fieldMethod) {
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
