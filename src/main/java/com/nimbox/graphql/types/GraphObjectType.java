package com.nimbox.graphql.types;

import static com.nimbox.graphql.utils.IntrospectionUtils.getAllInterfaces;
import static com.nimbox.graphql.utils.IntrospectionUtils.getAllSuperclasses;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nimbox.graphql.annotations.GraphQLField;
import com.nimbox.graphql.annotations.GraphQLInterface;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.registries.GraphRegistry.TypeAnnotation;
import com.nimbox.graphql.registries.GraphRegistry.TypeAnnotation.Content;

import graphql.com.google.common.base.Optional;

public class GraphObjectType {

	// properties

	private final Class<?> objectTypeClass;

	private final String name;
	private final String description;
	private final List<String> fieldOrder;

	private List<GraphInterfaceType> interfaces = new ArrayList<GraphInterfaceType>();

	private final Map<Method, GraphObjectTypeField> fields = new HashMap<Method, GraphObjectTypeField>();

	// constructors

	public GraphObjectType(final GraphRegistry registry, final Class<?> objectTypeClass) {

		TypeAnnotation<?>.Content annotation = registry.getTypeAnnotationOrThrow(objectTypeClass);

		// create

		this.objectTypeClass = objectTypeClass;

		this.name = annotation.getName();
		this.description = annotation.getDescription();
		this.fieldOrder = Arrays.asList(annotation.getFieldOrder());

		System.out.println("XXX: " + this.name);

		// extends interface

		for (Class<?> c : getAllSuperclasses(objectTypeClass)) {
			if (c.isAnnotationPresent(GraphQLInterface.class)) {
				GraphInterfaceType interfaceType = registry.getInterfaces().of(c);
				interfaceType.addImplementation(this);
				interfaces.add(interfaceType);
			}
		}

		// implements interfaces

		for (Class<?> c : getAllInterfaces(objectTypeClass)) {
			if (c.isAnnotationPresent(GraphQLInterface.class)) {
				GraphInterfaceType interfaceType = registry.getInterfaces().of(c);
				interfaceType.addImplementation(this);
				interfaces.add(interfaceType);
			}
		}

		// fields

		for (Method method : objectTypeClass.getMethods()) {
			if (registry.hasFieldAnnotation(method)) {
				System.out.println("     " + method);
				fields.put(method, new GraphObjectTypeField(registry, objectTypeClass, method));
			}
		}

	}

	// getters

	public Class<?> getObjectTypeClass() {
		return objectTypeClass;
	}

	public String getName() {
		return name;
	}

	public Optional<String> getDescription() {
		return Optional.of(description);
	}

	public List<String> getFieldOrder() {
		return fieldOrder;
	}

	public Map<Method, GraphObjectTypeField> getFields() {
		return fields;
	}

	public List<GraphInterfaceType> getInterfaces() {
		return interfaces;
	}

	// builder

	public graphql.schema.GraphQLObjectType.Builder newObjectType(GraphRegistry registry) {

		graphql.schema.GraphQLObjectType.Builder builder = graphql.schema.GraphQLObjectType.newObject();

		builder.name(name);
		if (description != null) {
			builder.description(description);
		}

		return builder;

	}

}
