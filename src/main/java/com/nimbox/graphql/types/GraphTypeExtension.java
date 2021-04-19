package com.nimbox.graphql.types;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nimbox.graphql.registries.GraphRegistry;

public abstract class GraphTypeExtension {

	// properties

	final Class<?> container;
	final Class<?> referenceContainer;

	final List<String> order;

	final Map<Method, GraphTypeExtensionField> fields = new LinkedHashMap<>();

	// constructors

	GraphTypeExtension(final GraphRegistry registry, final Class<?> container, final Data data) {

		// create

		this.container = container;
		this.referenceContainer = data.getType();

		this.order = data.getOrder();

		// fields

		for (Method method : container.getMethods()) {
			if (registry.getObjectExtensions().acceptTypeField(container, method)) {
				fields.put(method, new GraphTypeExtensionField(registry, container, method, this.referenceContainer));
			}
		}

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

	public Map<Method, GraphTypeExtensionField> getFields() {
		return fields;
	}

	// data

	public static interface Data {

		Class<?> getType();

		List<String> getOrder();

	}

}
