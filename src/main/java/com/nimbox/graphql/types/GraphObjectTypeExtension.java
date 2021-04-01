package com.nimbox.graphql.types;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nimbox.graphql.registries.GraphRegistry;

public class GraphObjectTypeExtension {

	// properties

	final Class<?> container;
	final Class<?> referenceContainer;

	final List<String> order;

	final Map<Method, GraphObjectTypeExtensionField> fields = new HashMap<Method, GraphObjectTypeExtensionField>();

	// constructors

	public GraphObjectTypeExtension(final GraphRegistry registry, final Class<?> container, final Data data) {

		// create

		this.container = container;
		this.referenceContainer = data.getType();

		this.order = data.getOrder();

		// fields

		for (Method method : container.getMethods()) {
			if (registry.getObjectExtensions().acceptTypeField(container, method)) {
				fields.put(method, new GraphObjectTypeExtensionField(registry, container, method, this.referenceContainer));
			}
		}

	}

	public GraphObjectTypeExtension(final GraphRegistry registry, final Class<?> container) {
		this(registry, container, registry.getObjectExtensions().extractTypeData(container));
	}

	// getters

	public Class<?> getContainer() {
		return container;
	}

	public Class<?> getReferenceContainer() {
		return referenceContainer;
	}

	public List<String> getOrder() {
		return order;
	}

	public Map<Method, GraphObjectTypeExtensionField> getFields() {
		return fields;
	}

	// data

	public static interface Data {

		Class<?> getType();

		List<String> getOrder();

	}

}
