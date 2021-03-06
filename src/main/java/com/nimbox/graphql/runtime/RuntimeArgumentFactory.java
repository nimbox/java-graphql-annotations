package com.nimbox.graphql.runtime;

import java.util.HashMap;
import java.util.Map;

import com.nimbox.graphql.types.GraphEnumType;
import com.nimbox.graphql.types.GraphInputObjectType;
import com.nimbox.graphql.types.GraphScalarType;

public class RuntimeArgumentFactory {

	private Map<Class<?>, RuntimeIdExtractor> ids = new HashMap<Class<?>, RuntimeIdExtractor>();
	private Map<Class<?>, RuntimeArgumentExtractor> extractors = new HashMap<Class<?>, RuntimeArgumentExtractor>();

	// constructors

	public RuntimeArgumentFactory() {

		extractors.put(Integer.class, new RuntimeArgumentExtractorGet());
		extractors.put(Long.class, new RuntimeArgumentExtractorGet());

		extractors.put(Double.class, new RuntimeArgumentExtractorGet());
		extractors.put(String.class, new RuntimeArgumentExtractorGet());
		extractors.put(Boolean.class, new RuntimeArgumentExtractorGet());
		extractors.put(String.class, new RuntimeArgumentExtractorGet());

	}

	public RuntimeArgumentFactory with(GraphScalarType scalarType) {
		extractors.computeIfAbsent(scalarType.getReferenceContainer(), k -> new RuntimeArgumentExtractorGet());
		return this;
	}

	public RuntimeArgumentFactory with(GraphEnumType enumType) {
		extractors.computeIfAbsent(enumType.getContainer(), k -> new RuntimeArgumentExtractorGet());
		return this;
	}

	public RuntimeArgumentFactory with(GraphInputObjectType inputObjectType) {
		extractors.computeIfAbsent(inputObjectType.getContainer(), k -> new RuntimeArgumentExtractorProxy(this, inputObjectType));
		return this;
	}

	// getters

	public Object get(Map<String, Object> arguments, RuntimeParameter parameter) throws Exception {
		return extractors.get(parameter.definition.getType()).apply(arguments, parameter);
	}

	// object overrides

	public String toString() {

		StringBuilder builder = new StringBuilder();

		for (Map.Entry<Class<?>, RuntimeArgumentExtractor> e : extractors.entrySet()) {
			builder.append(e.getKey()).append(" ").append(e.getValue()).append("\n");
		}

		return builder.toString();

	}

}
