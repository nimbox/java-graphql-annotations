package com.nimbox.graphql;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.nimbox.graphql.annotations.GraphQLType;

import graphql.Scalars;
import graphql.schema.GraphQLOutputType;

public class OutputTypeUtils {

	private OutputTypeUtils() {
	}

	/**
	 * Returns the first generic Class associated to the ParameterizedType. Can be
	 * used for return types and parameters.
	 * 
	 * @param type the ParameterizedType
	 * @return the first generic class
	 * @throws IllegalArgumentException if the type is not a ParameterizedType
	 */
	public static Class<?> genericTypeToClass(Type type) {

		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedReturnType = (ParameterizedType) type;
			Type[] arguments = parameterizedReturnType.getActualTypeArguments();
			return (Class<?>) arguments[0];
		}

		throw new IllegalArgumentException("Type is not a parameterized type");

	}

	/**
	 * Translates a given class to its corresponding GraphQLOutputType.
	 * 
	 * @param klass the class to convert
	 * @return the GraphQLOutputType
	 * @throws IllegalArgumentException if the class can not be converted to a
	 *                                  GraphQLOutputTypt
	 */
	public static GraphQLOutputType classToOutputType(Class<?> klass) {

		if (klass.equals(String.class)) {
			return Scalars.GraphQLString;
		}

		if (klass.equals(Integer.class)) {
			return Scalars.GraphQLInt;
		}

		if (klass.isAnnotationPresent(GraphQLType.class)) {
			GraphQLType annotation = klass.getAnnotation(GraphQLType.class);
			return graphql.schema.GraphQLTypeReference.typeRef(annotation.name());
		}

		throw new IllegalArgumentException("Return class is not valid for graphql");

	}

}
