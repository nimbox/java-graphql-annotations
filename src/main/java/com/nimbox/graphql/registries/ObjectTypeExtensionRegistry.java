package com.nimbox.graphql.registries;

import static com.nimbox.graphql.utils.IntrospectionUtils.getTypeAnnotationOrThrow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nimbox.graphql.annotations.GraphQLTypeExtension;
import com.nimbox.graphql.types.GraphObjectTypeExtension;

public class ObjectTypeExtensionRegistry {

	// properties

	private final GraphRegistry registry;
	private Map<Class<?>, GraphObjectTypeExtension> data = new HashMap<Class<?>, GraphObjectTypeExtension>();
	private Map<Class<?>, List<GraphObjectTypeExtension>> dataByTypeClass = new HashMap<Class<?>, List<GraphObjectTypeExtension>>();

	// constructors

	public ObjectTypeExtensionRegistry(GraphRegistry registry) {
		this.registry = registry;
	}

	public GraphObjectTypeExtension of(final Class<?> objectTypeExtensionClass) {

		if (data.containsKey(objectTypeExtensionClass)) {
			return data.get(objectTypeExtensionClass);
		}

		// check that it has the GraphQLType annotation.

		GraphQLTypeExtension annotation = getTypeAnnotationOrThrow(GraphQLTypeExtension.class, objectTypeExtensionClass);

		// create

		GraphObjectTypeExtension objectTypeExtension = new GraphObjectTypeExtension(registry, objectTypeExtensionClass);
		data.put(objectTypeExtensionClass, objectTypeExtension);
		dataByTypeClass.computeIfAbsent(objectTypeExtension.getObjectTypeClass(), k -> new ArrayList<GraphObjectTypeExtension>()).add(objectTypeExtension);

		// return

		return objectTypeExtension;

	}

	// getters

	public GraphObjectTypeExtension get(Class<?> object) {
		return data.get(object);
	}

	public List<GraphObjectTypeExtension> getForObjectType(Class<?> object) {
		return dataByTypeClass.getOrDefault(object, Collections.emptyList());
	}

	public Collection<GraphObjectTypeExtension> all() {
		return data.values();
	}

}
