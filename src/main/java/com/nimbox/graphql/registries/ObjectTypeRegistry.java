package com.nimbox.graphql.registries;

import static com.nimbox.graphql.utils.IntrospectionUtils.getTypeAnnotationOrThrow;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.nimbox.graphql.GeneratorException;
import com.nimbox.graphql.annotations.GraphQLType;
import com.nimbox.graphql.types.GraphObjectType;

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

		GraphQLType annotation = getTypeAnnotationOrThrow(GraphQLType.class, objectTypeClass);
		if (names.containsKey(annotation.name())) {
			throw new GeneratorException(String.format("Type %s has same name as type %s", objectTypeClass, names.get(annotation.name())));
		}

		// create

		GraphObjectType objectType = new GraphObjectType(registry, objectTypeClass);
		data.put(objectTypeClass, objectType);
		names.put(objectType.getName(), objectTypeClass);

		// return

		return objectType;

	}

	// getters

	public GraphObjectType get(Class<?> object) {
		return data.get(object);
	}

	public Collection<GraphObjectType> all() {
		return data.values();
	}

}
