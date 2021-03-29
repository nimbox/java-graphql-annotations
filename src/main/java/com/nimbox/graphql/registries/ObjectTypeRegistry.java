package com.nimbox.graphql.registries;

import static graphql.schema.GraphQLTypeReference.typeRef;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.nimbox.graphql.GraphBuilderException;
import com.nimbox.graphql.registries.GraphRegistry.TypeAnnotation;
import com.nimbox.graphql.types.GraphObjectType;

import graphql.schema.GraphQLOutputType;

public class ObjectTypeRegistry {

	// properties

	private final GraphRegistry registry;
	private Map<Class<?>, GraphObjectType> data = new HashMap<Class<?>, GraphObjectType>();
	private Map<String, Class<?>> names = new HashMap<String, Class<?>>();

	// constructors

	public ObjectTypeRegistry(GraphRegistry registry) {
		this.registry = registry;
	}

	public GraphObjectType of(final Class<?> objectTypeClass) {

		if (data.containsKey(objectTypeClass)) {
			return data.get(objectTypeClass);
		}

		// check the name is not duplicated

		TypeAnnotation<?>.Content annotation = registry.getTypeAnnotationOrThrow(objectTypeClass);
		if (names.containsKey(annotation.getName())) {
			throw new GraphBuilderException(String.format("Type %s has same name as type %s", objectTypeClass, names.get(annotation.getName())));
		}

		// create

		GraphObjectType objectType = new GraphObjectType(registry, objectTypeClass);
		data.put(objectTypeClass, objectType);
		names.put(objectType.getName(), objectTypeClass);

		// return

		return objectType;

	}

	// getters

	public boolean contains(Class<?> objectTypeClass) {
		return data.containsKey(objectTypeClass);
	}

	public GraphObjectType get(Class<?> objectTypeClass) {
		return data.get(objectTypeClass);
	}

	public GraphQLOutputType getGraphQLType(Class<?> objectTypeClass) {
		return typeRef(data.get(objectTypeClass).getName());
	}

	public Collection<GraphObjectType> all() {
		return data.values();
	}

}
