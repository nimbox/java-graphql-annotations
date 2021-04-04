package com.nimbox.graphql.runtime;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.lang.reflect.Method;
import java.util.List;

import com.nimbox.graphql.parameters.GraphParameter;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public abstract class RuntimeDataFetcher<T> implements DataFetcher<T> {

	// properties

	private final RuntimeParameterFactory factory;

	private final Method method;
	private final List<RuntimeParameter> parameters;

	// constructors

	public RuntimeDataFetcher(final RuntimeParameterFactory factory, final Method fieldMethod, final List<GraphParameter> parameters) {

		this.factory = factory;

		this.method = fieldMethod;
		this.parameters = parameters.stream().map(GraphParameter::getRuntimeParameter).collect(toUnmodifiableList());

	}

	// methods

	public abstract Object getSource(DataFetchingEnvironment environment) throws Exception;

	@Override
	@SuppressWarnings("unchecked")
	public T get(DataFetchingEnvironment environment) throws Exception {

		try {

			Object[] args = new Object[parameters.size()];
			for (int i = 0; i < parameters.size(); i++) {
				args[i] = factory.get(environment, parameters.get(i));
			}

			return (T) method.invoke(getSource(environment), args);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

}
