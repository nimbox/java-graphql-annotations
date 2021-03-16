package com.nimbox.graphql.parameters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

import com.nimbox.graphql.GeneratorException;
import com.nimbox.graphql.annotations.GraphQLArgument;
import com.nimbox.graphql.annotations.GraphQLContext;
import com.nimbox.graphql.annotations.GraphQLEnvironment;
import com.nimbox.graphql.annotations.GraphQLSource;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeParameter;
import com.nimbox.graphql.types.GraphValueClass;

import graphql.schema.DataFetchingEnvironment;

public abstract class GraphParameter {

	// properties

	protected final GraphValueClass valueClass;

	// constructors

	protected <T extends Annotation> GraphParameter(final GraphRegistry registry, final Parameter parameter, final GraphValueClass valueClass, final Class<T> annotationClass) {

		if (!parameter.isAnnotationPresent(annotationClass)) {
			throw new GeneratorException(String.format("Parameter %s expected to have annotation %s", //
					parameter.getName(), //
					annotationClass) //
			);
		}

		this.valueClass = valueClass;

	}

	public static GraphParameter of(final GraphRegistry registry, final Parameter fieldParameter) {

		if (fieldParameter.isAnnotationPresent(GraphQLArgument.class)) {
			return GraphParameterArgument.of(registry, fieldParameter);
		}

		if (fieldParameter.isAnnotationPresent(GraphQLContext.class)) {
			return new GraphParameterContext(registry, fieldParameter);
		}

		if (fieldParameter.isAnnotationPresent(GraphQLSource.class)) {
			return new GraphParameterSource(registry, fieldParameter);
		}

		if (fieldParameter.isAnnotationPresent(GraphQLEnvironment.class)) {
			return new GraphParameterEnvironment(registry, fieldParameter);
		}

		throw new GeneratorException(String.format("Parameter %s does not have a recognized annotation", fieldParameter.getName()));

	}

	// getters

	public GraphValueClass getValueClass() {
		return valueClass;
	}

	public abstract RuntimeParameter getRuntimeParameter();

	public abstract <T> T getParameterValue(DataFetchingEnvironment environment) throws Exception;

}
