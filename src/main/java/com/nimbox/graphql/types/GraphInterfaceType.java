package com.nimbox.graphql.types;

import static com.nimbox.graphql.utils.IntrospectionUtils.getTypeAnnotationOrThrow;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nimbox.graphql.annotations.GraphQLField;
import com.nimbox.graphql.annotations.GraphQLInterface;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeTypeResolver;
import com.nimbox.graphql.utils.ReservedStrings;

import graphql.com.google.common.base.Optional;
import graphql.schema.TypeResolver;

public class GraphInterfaceType {

	// properties

	private final Class<?> interfaceTypeClass;
	private final List<GraphObjectType> implementations = new ArrayList<GraphObjectType>();

	private final String name;
	private final String description;
	private final List<String> fieldOrder;

	private final Map<Method, GraphObjectTypeField> fields = new HashMap<Method, GraphObjectTypeField>();

	// constructors

	public GraphInterfaceType(final GraphRegistry registry, final Class<?> interfaceTypeClass) {

		GraphQLInterface annotation = getTypeAnnotationOrThrow(GraphQLInterface.class, interfaceTypeClass);

		// create

		this.interfaceTypeClass = interfaceTypeClass;

		this.name = annotation.name();
		this.description = ReservedStrings.translate(annotation.description());
		this.fieldOrder = Arrays.asList(annotation.fieldOrder());

		// fields

		for (Method method : interfaceTypeClass.getMethods()) {
			if (method.isAnnotationPresent(GraphQLField.class)) {
				fields.put(method, new GraphObjectTypeField(registry, interfaceTypeClass, method));
			}
		}

	}

	// getters

	public Class<?> getInterfaceTypeClass() {
		return interfaceTypeClass;
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

	public void addImplementation(GraphObjectType objectType) {
		implementations.add(objectType);
	}

	public List<GraphObjectType> getImplementations() {
		return implementations;
	}

	// builder

	public graphql.schema.GraphQLInterfaceType.Builder newInterfaceType(GraphRegistry registry) {

		graphql.schema.GraphQLInterfaceType.Builder builder = graphql.schema.GraphQLInterfaceType.newInterface();

		builder.name(name);
		if (description != null) {
			builder.description(description);
		}

		return builder;

	}

	public TypeResolver getResolver() {
		return new RuntimeTypeResolver(interfaceTypeClass, implementations);
	}

}
