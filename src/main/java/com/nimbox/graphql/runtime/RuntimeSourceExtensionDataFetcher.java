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

	public RuntimeSourceExtensionDataFetcher(final RuntimeParameterFactory factory, final Class<?> container, final Method method, final List<GraphParameter> parameters, Class<?> referenceContainer) {
		super(factory, method, parameters);
		this.constructor = getClassConstructorOrThrow(container, referenceContainer);
	}

	// methods

	public Object getSource(DataFetchingEnvironment environment) throws Exception {
		return constructor.newInstance(new Object[] { environment.getSource() });
	}

}
