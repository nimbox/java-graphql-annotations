package com.nimbox.graphql.types;

import static com.nimbox.graphql.utils.IntrospectionUtils.getTypeAnnotationOrThrow;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nimbox.graphql.annotations.GraphQLQuery;
import com.nimbox.graphql.annotations.GraphQLType;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.utils.ReservedStrings;

import graphql.com.google.common.base.Optional;

public class GraphObjectType {

	// properties

	private final Class<?> objectTypeClass;

	private final String name;
	private final String description;
	private final List<String> fieldOrder;

	private final Map<Method, GraphObjectTypeField> fields = new HashMap<Method, GraphObjectTypeField>();

	// constructors

	public GraphObjectType(final GraphRegistry registry, final Class<?> objectTypeClass) {

		GraphQLType annotation = getTypeAnnotationOrThrow(GraphQLType.class, objectTypeClass);

		// create

		this.objectTypeClass = objectTypeClass;

		this.name = annotation.name();
		this.description = ReservedStrings.translate(annotation.description());
		this.fieldOrder = Arrays.asList(annotation.fieldOrder());

		// fields

		for (Method method : objectTypeClass.getMethods()) {
			if (method.isAnnotationPresent(GraphQLQuery.class)) {
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
