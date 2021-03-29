package com.nimbox.graphql.registries;

import static com.nimbox.graphql.utils.IntrospectionUtils.getTypeAnnotationOrThrow;
import static graphql.schema.GraphQLTypeReference.typeRef;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.nimbox.graphql.GraphBuilderException;
import com.nimbox.graphql.annotations.GraphQLInterface;
import com.nimbox.graphql.types.GraphInterfaceType;

import graphql.schema.GraphQLOutputType;

public class InterfaceTypeRegistry {

	// properties

	private final GraphRegistry registry;
	private Map<Class<?>, GraphInterfaceType> data = new HashMap<Class<?>, GraphInterfaceType>();

	private Map<String, Class<?>> names = new HashMap<String, Class<?>>();

	// constructors

	public InterfaceTypeRegistry(GraphRegistry registry) {
		this.registry = registry;
	}

	public GraphInterfaceType of(final Class<?> interfaceTypeClass) {

		if (data.containsKey(interfaceTypeClass)) {
			return data.get(interfaceTypeClass);
		}

		// check the name is not duplicated

		GraphQLInterface annotation = getTypeAnnotationOrThrow(GraphQLInterface.class, interfaceTypeClass);
		if (names.containsKey(annotation.name())) {
			throw new GraphBuilderException(String.format("Interface %s has same name as interface %s", interfaceTypeClass, names.get(annotation.name())));
		}

		// create

		GraphInterfaceType interfaceType = new GraphInterfaceType(registry, interfaceTypeClass);
		data.put(interfaceTypeClass, interfaceType);
		names.put(interfaceType.getName(), interfaceTypeClass);

		// return

		return interfaceType;

	}

	// getters

	public boolean contains(Class<?> interfaceTypeClass) {
		return data.containsKey(interfaceTypeClass);
	}

	public GraphInterfaceType get(Class<?> interfaceTypeClass) {
		return data.get(interfaceTypeClass);
	}

	public GraphQLOutputType getGraphQLType(Class<?> interfaceTypeClass) {
		return typeRef(data.get(interfaceTypeClass).getName());
	}

	public Collection<GraphInterfaceType> all() {
		return data.values();
	}

}
