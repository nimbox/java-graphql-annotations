package com.nimbox.graphql.runtime;

import static graphql.Scalars.GraphQLBoolean;
import static graphql.Scalars.GraphQLFloat;
import static graphql.Scalars.GraphQLID;
import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLString;

import java.util.HashMap;
import java.util.Map;

import com.nimbox.graphql.types.GraphEnumType;
import com.nimbox.graphql.types.GraphInputObjectType;
import com.nimbox.graphql.types.GraphScalarType;

public class ArgumentFactory {

	private Map<Class<?>, ArgumentExtractor> extractors = new HashMap<Class<?>, ArgumentExtractor>();

	// constructors

	public ArgumentFactory() {

		extractors.put(Integer.class, new ArgumentExtractorDirect());
		extractors.put(Double.class, new ArgumentExtractorDirect());
		extractors.put(String.class, new ArgumentExtractorDirect());
		extractors.put(Boolean.class, new ArgumentExtractorDirect());
		extractors.put(String.class, new ArgumentExtractorDirect());

	}

	public ArgumentFactory with(GraphScalarType scalarType) {
		extractors.computeIfAbsent(scalarType.getScalarTypeClass(), k -> new ArgumentExtractorDirect());
		return this;
	}

	public ArgumentFactory with(GraphEnumType enumType) {
		extractors.computeIfAbsent(enumType.getEnumTypeClass(), k -> new ArgumentExtractorDirect());
		return this;
	}

	public ArgumentFactory with(GraphInputObjectType inputObjectType) {
		extractors.computeIfAbsent(inputObjectType.getInputObjectTypeClass(), k -> new ArgumentExtractorProxy(this, inputObjectType));
		return this;
	}

	// getters

	public Object get(Map<String, Object> arguments, RuntimeParameter parameter) throws Exception {

		System.out.println("XXX: " + parameter.valueClass.getValueClass());

		return extractors.get(parameter.valueClass.getValueClass()).apply(arguments, parameter);

	}

	// object overrides

	public String toString() {

		StringBuilder builder = new StringBuilder();

		for (Map.Entry<Class<?>, ArgumentExtractor> e : extractors.entrySet()) {
			builder.append(e.getKey()).append(" ").append(e.getValue()).append("\n");
		}

		return builder.toString();

	}

}
