package com.nimbox.graphql.parameters;

import java.lang.reflect.Parameter;

import com.nimbox.graphql.GeneratorException;
import com.nimbox.graphql.annotations.GraphQLEnvironment;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeParameter;
import com.nimbox.graphql.types.GraphValueClass;

import graphql.schema.DataFetchingEnvironment;

public class GraphParameterEnvironment extends GraphParameter {

	// constructors

	public GraphParameterEnvironment(final GraphRegistry registry, final Parameter fieldParameter) {
		super(registry, fieldParameter, new GraphValueClass(fieldParameter.getType()), GraphQLEnvironment.class);

		if (valueClass.isOptional() || valueClass.isList() || !DataFetchingEnvironment.class.isAssignableFrom(this.valueClass.getValueClass())) {
			throw new GeneratorException(String.format("Parameter %s annotated with %s must have type %s", //
					fieldParameter.getName(), //
					GraphQLEnvironment.class, //
					DataFetchingEnvironment.class) //
			);
		}

	}

	// getters

	@Override
	@SuppressWarnings("unchecked")
	public DataFetchingEnvironment getParameterValue(DataFetchingEnvironment environment) {
		return environment;
	}

	@Override
	public RuntimeParameter getRuntimeParameter() {
		return new RuntimeParameter(valueClass);
	}

	// object overrides

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("@").append(GraphQLEnvironment.class);

		return builder.toString();

	}

}
