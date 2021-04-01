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

	private final Class<?> container;
	private final Map<Class<?>, String> implementations = new HashMap<Class<?>, String>();

	// constructors

	public RuntimeTypeResolver(Class<?> container, List<GraphObjectType> implementations) {

		this.container = container;
		for (GraphObjectType referenceContainer : implementations) {
			this.implementations.put(referenceContainer.getContainer(), referenceContainer.getName());
		}

	}

	// getters

	@Override
	public GraphQLObjectType getType(TypeResolutionEnvironment environment) {

		Object object = environment.getObject();

		String name = implementations.get(object.getClass());
		if (name != null) {
			return environment.getSchema().getObjectType(name);
		}

		throw new IllegalArgumentException(String.format("Unable to resolve %s as interface %s", object.getClass(), container));

	}

}
