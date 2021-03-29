package com.nimbox.graphql.registries;

import static graphql.schema.GraphQLTypeReference.typeRef;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.nimbox.graphql.GraphBuilderException;
import com.nimbox.graphql.annotations.GraphQLInput;
import com.nimbox.graphql.types.GraphInputObjectType;

import graphql.schema.GraphQLTypeReference;

public class InputObjectTypeRegistry {

	// properties

	private final GraphRegistry registry;
	private final Map<Class<?>, GraphInputObjectType> data = new HashMap<Class<?>, GraphInputObjectType>();
	private Map<String, Class<?>> names = new HashMap<String, Class<?>>();

	// constructors

	public InputObjectTypeRegistry(GraphRegistry registry) {
		this.registry = registry;
	}

	public GraphInputObjectType of(Class<?> typeClass) {

		if (data.containsKey(typeClass)) {
			return data.get(typeClass);
		}

		// check that it has the GraphQLInput annotation.

		GraphQLInput annotation = typeClass.getAnnotation(GraphQLInput.class);
		if (annotation == null) {
			annotation = typeClass.getAnnotatedSuperclass().getAnnotation(GraphQLInput.class);
		}
		if (annotation == null) {
			throw new GraphBuilderException(String.format("Type %s does not have annotation %s", typeClass, GraphQLInput.class));
		}

		// check the name is not duplicated

		if (names.containsKey(annotation.name())) {
			throw new GraphBuilderException(String.format("Type %s has same name as type %s", typeClass, names.get(annotation.name())));
		}

		// create

		GraphInputObjectType inputObject = new GraphInputObjectType(registry, typeClass);
		data.put(typeClass, inputObject);
		names.put(inputObject.getName(), typeClass);

		// return

		return inputObject;

	}

	// getters

	public boolean contains(Class<?> inputObjectTypeClass) {
		return data.containsKey(inputObjectTypeClass);
	}

	public GraphInputObjectType get(Class<?> object) {
		return data.get(object);
	}

	public GraphQLTypeReference getGraphQLType(Class<?> inputObjectTypeClass) {
		return typeRef(data.get(inputObjectTypeClass).getName());
	}

	public Collection<GraphInputObjectType> all() {
		return data.values();
	}

}
