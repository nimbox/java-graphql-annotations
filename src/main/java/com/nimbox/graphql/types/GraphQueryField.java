package com.nimbox.graphql.types;

import static com.nimbox.graphql.utils.IntrospectionUtils.getAnnotationOrThrow;

import java.lang.reflect.Method;

import com.nimbox.graphql.annotations.GraphQLQuery;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeInstanceDataFetcher;
import com.nimbox.graphql.runtime.RuntimeParameterFactory;
import com.nimbox.graphql.utils.ReservedStrings;

import graphql.schema.DataFetcher;

public class GraphQueryField extends GraphField {

	// constructor

	public GraphQueryField(final GraphRegistry registry, Class<?> typeClass, final Method fieldMethod) {
		super(registry, new QueryAnnotation(fieldMethod), typeClass, fieldMethod);
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

		builder.append("@").append(GraphQLQuery.class.getSimpleName()).append("(").append("name").append(" = ").append(name).append(")");
		builder.append(" ");
		builder.append(container);

		return builder.toString();

	}

	// classes

	public static class QueryAnnotation implements TypeAnnotation {

		private final GraphQLQuery annotation;

		public QueryAnnotation(final Method fieldMethod) {
			annotation = getAnnotationOrThrow(GraphQLQuery.class, fieldMethod);
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
