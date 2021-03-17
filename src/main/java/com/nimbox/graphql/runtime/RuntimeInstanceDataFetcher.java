package com.nimbox.graphql.runtime;

import static com.nimbox.graphql.utils.IntrospectionUtils.getClassInstanceOrThrow;

import java.lang.reflect.Method;
import java.util.List;

import com.nimbox.graphql.parameters.GraphParameter;

import graphql.schema.DataFetchingEnvironment;

public class RuntimeInstanceDataFetcher<T> extends RuntimeDataFetcher<T> {

	// properties

	private final Object instance;

	// constructors

	public RuntimeInstanceDataFetcher(final RuntimeParameterFactory factory, final Class<?> typeClass, final Method fieldMethod, final List<GraphParameter> parameters) {
		super(factory, fieldMethod, parameters);
		this.instance = getClassInstanceOrThrow(typeClass);
	}

	// methods

	public Object getSource(DataFetchingEnvironment environment) {
		return instance;
	}

}
