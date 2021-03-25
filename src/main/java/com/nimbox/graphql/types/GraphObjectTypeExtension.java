package com.nimbox.graphql.types;

import static com.nimbox.graphql.utils.IntrospectionUtils.getTypeAnnotationOrThrow;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nimbox.graphql.annotations.GraphQLField;
import com.nimbox.graphql.annotations.GraphQLTypeExtension;
import com.nimbox.graphql.registries.GraphRegistry;

public class GraphObjectTypeExtension {

	// properties

	private final Class<?> objectTypeClass;
	private final List<String> fieldOrder;

	private final Map<Method, GraphObjectTypeExtensionField> fields = new HashMap<Method, GraphObjectTypeExtensionField>();

	// constructors

	public GraphObjectTypeExtension(final GraphRegistry registry, final Class<?> objectTypeExtensionClass) {

		GraphQLTypeExtension annotation = getTypeAnnotationOrThrow(GraphQLTypeExtension.class, objectTypeExtensionClass);

		// create

		this.objectTypeClass = annotation.type();
		this.fieldOrder = Arrays.asList(annotation.fieldOrder());

		// fields

		for (Method method : objectTypeExtensionClass.getMethods()) {
			if (method.isAnnotationPresent(GraphQLField.class)) {
				fields.put(method, new GraphObjectTypeExtensionField(registry, objectTypeExtensionClass, method, this.objectTypeClass));
			}
		}

	}

	// getters

	public Class<?> getObjectTypeClass() {
		return objectTypeClass;
	}

	public List<String> getFieldOrder() {
		return fieldOrder;
	}

	public Map<Method, GraphObjectTypeExtensionField> getFields() {
		return fields;
	}

}
