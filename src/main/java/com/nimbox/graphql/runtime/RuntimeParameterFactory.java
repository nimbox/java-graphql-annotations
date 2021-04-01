package com.nimbox.graphql.runtime;

import java.util.HashMap;
import java.util.Map;

import com.nimbox.graphql.types.GraphEnumType;
import com.nimbox.graphql.types.GraphInputObjectType;
import com.nimbox.graphql.types.GraphScalarType;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class RuntimeParameterFactory {

	// properties

	private Map<Class<?>, Map<String, DataFetcher<?>>> extractors = new HashMap<Class<?>, Map<String, DataFetcher<?>>>();
	private RuntimeArgumentFactory arguments = new RuntimeArgumentFactory();

	// constructors

	public RuntimeParameterFactory() {
	}

	public <T> RuntimeParameterFactory with(Class<T> context, String name, DataFetcher<T> fetcher) {
		extractors.computeIfAbsent(context, c -> new HashMap<String, DataFetcher<?>>()).put(name, fetcher);
		return this;
	}

	//

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

		if (extractors.containsKey(parameter.definition.getType())) {
			return (T) extractors.get(parameter.definition.getType()).get(parameter.name).get(environment);
		}

		return (T) arguments.get(environment.getArguments(), parameter);

	}

	// object overrides

	public String toString() {

		StringBuilder builder = new StringBuilder();

		for (Map.Entry<Class<?>, Map<String, DataFetcher<?>>> e : extractors.entrySet()) {
			builder.append(e.getKey()).append(" ").append(e.getValue()).append("\n");
		}
		builder.append(arguments.toString());

		return builder.toString();

	}

}
