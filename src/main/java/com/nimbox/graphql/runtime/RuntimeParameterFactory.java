package com.nimbox.graphql.runtime;

import java.util.HashMap;
import java.util.Map;

import com.nimbox.graphql.types.GraphEnumType;
import com.nimbox.graphql.types.GraphInputObjectType;
import com.nimbox.graphql.types.GraphScalarType;

import graphql.schema.DataFetchingEnvironment;

public class RuntimeParameterFactory {

	// properties

	private Map<Class<?>, RuntimeParameterExtractor> extractors = new HashMap<Class<?>, RuntimeParameterExtractor>();
	private ArgumentFactory arguments = new ArgumentFactory();

	// constructors

	public RuntimeParameterFactory() {

		extractors.put(DataFetchingEnvironment.class, new RuntimeParameterExtractor() {

			@Override
			public Object apply(DataFetchingEnvironment environment, RuntimeParameter parameter) throws Exception {
				return environment;
			}

			public String toString() {
				return DataFetchingEnvironment.class.toString();
			}

		});

	}

	public RuntimeParameterFactory with(GraphScalarType scalarType) {
		arguments.with(scalarType);
		return this;
	}

	public RuntimeParameterFactory with(GraphEnumType enumType) {
		arguments.with(enumType);
		return this;
	}

	public RuntimeParameterFactory with(GraphInputObjectType inputObjectType) {
		arguments.with(inputObjectType);
		return this;
	}

	// methods

	@SuppressWarnings("unchecked")
	public <T> T get(DataFetchingEnvironment environment, RuntimeParameter parameter) throws Exception {

		if (extractors.containsKey(parameter.valueClass.getValueClass())) {
			return (T) extractors.get(parameter.valueClass.getValueClass()).apply(environment, parameter);
		}

		return (T) arguments.get(environment.getArguments(), parameter);

	}

	// object overrides

	public String toString() {

		StringBuilder builder = new StringBuilder();

		for (Map.Entry<Class<?>, RuntimeParameterExtractor> e : extractors.entrySet()) {
			builder.append(e.getKey()).append(" ").append(e.getValue()).append("\n");
		}
		builder.append(arguments.toString());

		return builder.toString();

	}

}
