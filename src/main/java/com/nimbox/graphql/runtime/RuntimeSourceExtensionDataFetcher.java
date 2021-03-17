package com.nimbox.graphql.runtime;

import static com.nimbox.graphql.utils.IntrospectionUtils.getClassConstructorOrThrow;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import com.nimbox.graphql.parameters.GraphParameter;

import graphql.schema.DataFetchingEnvironment;

public class RuntimeSourceExtensionDataFetcher<T> extends RuntimeDataFetcher<T> {

	// properties

	private final Constructor<?> constructor;

	// constructors

	public RuntimeSourceExtensionDataFetcher(final RuntimeParameterFactory factory, final Class<?> typeClass, final Method fieldMethod, final List<GraphParameter> parameters, Class<?> objectTypeClass) {
		super(factory, fieldMethod, parameters);
		this.constructor = getClassConstructorOrThrow(typeClass, objectTypeClass);
	}

	// methods

	public Object getSource(DataFetchingEnvironment environment) throws Exception {
		return constructor.newInstance(environment.getSource());
	}

}
