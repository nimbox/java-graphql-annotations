package com.nimbox.graphql.registries;

import static com.nimbox.graphql.utils.IntrospectionUtils.getTypeAnnotationOrThrow;
import static graphql.Scalars.GraphQLBoolean;
import static graphql.Scalars.GraphQLFloat;
import static graphql.Scalars.GraphQLID;
import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLString;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.nimbox.graphql.GeneratorException;
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
		defaults.put(Double.class, GraphQLFloat);
		defaults.put(String.class, GraphQLString);
		defaults.put(Boolean.class, GraphQLBoolean);
		defaults.put(String.class, GraphQLID);

	}

	public GraphScalarType of(final Class<?> scalarTypeClass) {

		if (data.containsKey(scalarTypeClass)) {
			return data.get(scalarTypeClass);
		}

		// check the name is not duplicated

		GraphQLScalar annotation = getTypeAnnotationOrThrow(GraphQLScalar.class, scalarTypeClass);
		if (names.containsKey(annotation.name())) {
			throw new GeneratorException(String.format("Type %s has same name as type %s", scalarTypeClass, names.get(annotation.name())));
		}

		// create

		GraphScalarType scalarType = new GraphScalarType(registry, scalarTypeClass);
		data.put(scalarTypeClass, scalarType);
		names.put(scalarType.getName(), scalarTypeClass);

		// return

		return scalarType;

	}

	// methods

	public boolean contains(Class<?> valueType) {
		return defaults.containsKey(valueType) || data.containsKey(valueType);
	}

	public GraphQLScalarType getScalarType(Class<?> valueClass) {
		return defaults.get(valueClass);
	}

	public Collection<GraphScalarType> all() {
		return data.values();
	}

}
