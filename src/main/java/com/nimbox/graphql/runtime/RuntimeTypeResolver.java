package com.nimbox.graphql.runtime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nimbox.graphql.types.GraphObjectType;

import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;

public class RuntimeTypeResolver implements TypeResolver {

	// properties

	private final Class<?> interfaceTypeClass;
	private final Map<Class<?>, String> types = new HashMap<Class<?>, String>();

	// constructors

	public RuntimeTypeResolver(Class<?> interfaceTypeClass, List<GraphObjectType> implementations) {

		this.interfaceTypeClass = interfaceTypeClass;
		for (GraphObjectType objectType : implementations) {
			this.types.put(objectType.getObjectTypeClass(), objectType.getName());
		}

	}

	// getters

	@Override
	public GraphQLObjectType getType(TypeResolutionEnvironment environment) {

		Object object = environment.getObject();

		String name = types.get(object.getClass());
		if (name != null) {
			return environment.getSchema().getObjectType(name);
		}

		throw new IllegalArgumentException(String.format("Unable to resolve %s as interface %s", object.getClass(), interfaceTypeClass));

	}

}
