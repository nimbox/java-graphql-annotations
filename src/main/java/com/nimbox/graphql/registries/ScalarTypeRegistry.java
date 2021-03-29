package com.nimbox.graphql.registries;

import static com.nimbox.graphql.utils.IntrospectionUtils.getTypeAnnotationOrThrow;
import static graphql.Scalars.GraphQLBoolean;
import static graphql.Scalars.GraphQLFloat;
import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLString;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.nimbox.graphql.GraphBuilderException;
import com.nimbox.graphql.annotations.GraphQLScalar;
import com.nimbox.graphql.types.GraphScalarType;

import graphql.schema.GraphQLScalarType;

public class ScalarTypeRegistry {

	// properties

	private final GraphRegistry registry;

	private Map<Class<?>, GraphQLScalarType> defaults = new HashMap<Class<?>, GraphQLScalarType>();

	private Map<Class<?>, GraphScalarType> data = new HashMap<Class<?>, GraphScalarType>();
	private Map<String, Class<?>> names = new HashMap<String, Class<?>>();

	// constructors

	public ScalarTypeRegistry(GraphRegistry registry) {
		this.registry = registry;

		defaults.put(Integer.class, GraphQLInt);
		defaults.put(Long.class, GraphQLInt);

		defaults.put(Double.class, GraphQLFloat);
		defaults.put(BigDecimal.class, GraphQLFloat);

		defaults.put(String.class, GraphQLString);
		defaults.put(Boolean.class, GraphQLBoolean);

	}

	public GraphScalarType of(final Class<?> javaClass, final Class<?> scalarCoercingClass) {

		if (data.containsKey(javaClass)) {
			return data.get(javaClass);
		}

		// check the name is not duplicated

		GraphQLScalar annotation = getTypeAnnotationOrThrow(GraphQLScalar.class, scalarCoercingClass);
		if (names.containsKey(annotation.name())) {
			throw new GraphBuilderException(String.format("Type %s has same name as type %s", javaClass, names.get(annotation.name())));
		}

		// create

		GraphScalarType scalarType = new GraphScalarType(registry, javaClass, scalarCoercingClass);
		data.put(javaClass, scalarType);
		names.put(scalarType.getName(), javaClass);

		// return

		return scalarType;

	}

	// getters

	public boolean contains(Class<?> scalarTypeClass) {
		return defaults.containsKey(scalarTypeClass) || data.containsKey(scalarTypeClass);
	}

	public GraphQLScalarType getGraphQLType(Class<?> valueClass) {

		if (defaults.containsKey(valueClass)) {
			return defaults.get(valueClass);
		}

		if (data.containsKey(valueClass)) {
			GraphScalarType scalarType = data.get(valueClass);
			return scalarType.built();
		}

		return null;

	}

	public Collection<GraphScalarType> all() {
		return data.values();
	}

}
