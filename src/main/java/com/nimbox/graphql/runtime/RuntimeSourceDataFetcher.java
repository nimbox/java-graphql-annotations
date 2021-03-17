package com.nimbox.graphql.runtime;

import java.lang.reflect.Method;
import java.util.List;

import com.nimbox.graphql.parameters.GraphParameter;

import graphql.schema.DataFetchingEnvironment;

public class RuntimeSourceDataFetcher<T> extends RuntimeDataFetcher<T> {

	// constructors
	
	public RuntimeSourceDataFetcher(final RuntimeParameterFactory factory, final Class<?> typeClass, final Method fieldMethod, final List<GraphParameter> parameters) {
		super(factory, fieldMethod, parameters);

	}

	// methods
	
	public Object getSource(DataFetchingEnvironment environment) {
		return environment.getSource();
	}

}
