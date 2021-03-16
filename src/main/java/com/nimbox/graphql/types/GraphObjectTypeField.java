package com.nimbox.graphql.types;

import java.lang.reflect.Method;

import com.nimbox.graphql.registries.GraphRegistry;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class GraphObjectTypeField extends GraphField {

	// constructor

	public GraphObjectTypeField(final GraphRegistry registry, final Class<?> typeClass, final Method fieldMethod) {
		super(registry, typeClass, fieldMethod);
	}

	// methods

	public DataFetcher<?> getFetcher() {

		return new DataFetcher<>() {

			@Override
			public Object get(DataFetchingEnvironment environment) throws Exception {

				Object[] args = new Object[parameters.size()];
				for (int i = 0; i < parameters.size(); i++) {
					args[i] = parameters.get(i).getParameterValue(environment);
				}

				Object source = environment.getSource();
				if (source == null) {
					return fieldMethod.invoke(objectTypeClass.getDeclaredConstructor().newInstance(), args);
				} else {
					return fieldMethod.invoke(source, args);
				}

			}

		};

	}

}
