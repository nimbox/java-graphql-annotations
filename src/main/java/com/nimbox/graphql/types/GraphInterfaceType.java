package com.nimbox.graphql.types;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeTypeResolver;

import graphql.schema.TypeResolver;

public class GraphInterfaceType {

	// properties

	private final Class<?> container;

	private final List<GraphObjectType> implementations = new ArrayList<GraphObjectType>();

	private final String name;
	private final String description;
	private final List<String> order;

	private final Map<Method, GraphInterfaceTypeField> fields = new HashMap<Method, GraphInterfaceTypeField>();

	// constructors

	public GraphInterfaceType(final GraphRegistry registry, final Class<?> container, Data data) {

		// create

		this.container = container;

		this.name = registry.name(data.getName(), container);
		this.description = data.getDescription();
		this.order = data.getOrder();

		// fields

		for (Method method : container.getMethods()) {
			if (registry.getInterfaces().acceptTypeField(container, method)) {
				fields.put(method, new GraphInterfaceTypeField(registry, container, method));
			}
		}

	}

	public GraphInterfaceType(final GraphRegistry registry, final Class<?> container) {
		this(registry, container, registry.getInterfaces().extractTypeData(container));
	}

	// getters

	public Class<?> getContainer() {
		return container;
	}

	public String getName() {
		return name;
	}

	public Optional<String> getDescription() {
		return Optional.ofNullable(description);
	}

	public List<String> getOrder() {
		return order;
	}

	public Map<Method, GraphInterfaceTypeField> getFields() {
		return fields;
	}

	//

	public void addImplementation(GraphObjectType objectType) {
		implementations.add(objectType);
	}

	public List<GraphObjectType> getImplementations() {
		return implementations;
	}

	// builder

	public graphql.schema.GraphQLInterfaceType.Builder newInterfaceType(GraphRegistry registry) {

		graphql.schema.GraphQLInterfaceType.Builder builder = graphql.schema.GraphQLInterfaceType.newInterface();

		builder.name(getName());
		getDescription().ifPresent(builder::description);

		return builder;

	}

	public TypeResolver getTypeResolver() {
		return new RuntimeTypeResolver(container, implementations);
	}

	// data

	public static interface Data {

		String getName();

		String getDescription();

		List<String> getOrder();

	}

}
