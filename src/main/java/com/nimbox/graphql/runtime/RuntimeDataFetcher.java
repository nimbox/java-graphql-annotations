package com.nimbox.graphql.runtime;

import static com.nimbox.graphql.utils.IntrospectionUtils.getClassConstructorOrThrow;
import static java.util.stream.Collectors.toUnmodifiableList;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import com.nimbox.graphql.parameters.GraphParameter;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class RuntimeDataFetcher<T> implements DataFetcher<T> {

	private final RuntimeParameterFactory factory;

	private final Constructor<?> classConstructor;
	private final Method fieldMethod;
	private final List<RuntimeParameter> parameters;

	public RuntimeDataFetcher(final RuntimeParameterFactory factory, final Class<?> typeClass, final Method fieldMethod, final List<GraphParameter> parameters) {

		this.factory = factory;

		this.classConstructor = getClassConstructorOrThrow(typeClass);
		this.fieldMethod = fieldMethod;
		this.parameters = parameters.stream().map(GraphParameter::getRuntimeParameter).collect(toUnmodifiableList());

	}

	@Override
	@SuppressWarnings("unchecked")
	public T get(DataFetchingEnvironment environment) throws Exception {

		Object[] args = new Object[parameters.size()];
		for (int i = 0; i < parameters.size(); i++) {
			args[i] = factory.get(environment, parameters.get(i));
		}

		Object source = classConstructor.newInstance();
		return (T) fieldMethod.invoke(source, args);

	}

}
