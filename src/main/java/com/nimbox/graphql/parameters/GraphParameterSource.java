package com.nimbox.graphql.parameters;

import java.lang.reflect.Parameter;

import com.nimbox.graphql.GeneratorException;
import com.nimbox.graphql.annotations.GraphQLSource;
import com.nimbox.graphql.annotations.GraphQLType;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeParameter;
import com.nimbox.graphql.types.GraphValueClass;

import graphql.schema.DataFetchingEnvironment;

public class GraphParameterSource extends GraphParameter {

	// constructors

	public GraphParameterSource(GraphRegistry registry, Parameter fieldParameter) {
		super(registry, fieldParameter, new GraphValueClass(fieldParameter.getType()), GraphQLSource.class);

		if (valueClass.isOptional() || valueClass.isList() || !this.valueClass.getValueClass().isAnnotationPresent(GraphQLType.class)) {
			throw new GeneratorException(String.format("Parameter %s annotated with %s must reference a class annotated with %s", //
					fieldParameter.getName(), //
					GraphQLSource.class, //
					GraphQLType.class) //
			);
		}

	}

	// getters

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getParameterValue(DataFetchingEnvironment environment) {
		return (T) environment.getSource();
	}

	@Override
	public RuntimeParameter getRuntimeParameter() {
		return new RuntimeParameter(valueClass);
	}

	// object overrides

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("@").append(GraphQLSource.class.getSimpleName());
		builder.append(" ");
		builder.append(valueClass.getValueClass());

		return builder.toString();

	}

}
