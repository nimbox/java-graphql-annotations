package com.nimbox.graphql.types;

import static com.nimbox.graphql.utils.IntrospectionUtils.getClassConstructorOrThrow;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.nimbox.graphql.registries.GraphRegistry;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class GraphQueryField extends GraphField {

	// properties

	protected final Constructor<?> constructor;

	// constructor

	public GraphQueryField(final GraphRegistry registry, final Class<?> typeClass, final Method fieldMethod) {
		super(registry, typeClass, fieldMethod);

		this.constructor = getClassConstructorOrThrow(typeClass);

	}

	// methods

	public DataFetcher<?> getFetcher() {

		return new DataFetcher<>() {

			@Override
			public Object get(DataFetchingEnvironment environment) throws Exception {

				Object[] args = new Object[parameters.size()];
				for (int i = 0; i < parameters.size(); i++) {
					args[i] = parameters.get(i).getParameterValue(environment);
					System.out.println("args[i]: " + args[i]);
				}

				Object source = constructor.newInstance();
				return fieldMethod.invoke(source, args);

			}

		};

	}

}
