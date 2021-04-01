package com.nimbox.graphql.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeTypeResolver;

import graphql.schema.TypeResolver;

public class GraphUnionType {

	// properties

	private final Class<?> container;

	private final List<GraphObjectType> implementations = new ArrayList<GraphObjectType>();

	private final String name;
	private final String description;

	// constructors

	public GraphUnionType(final GraphRegistry registry, final Class<?> container, final Data data) {

		// create

		this.container = container;

		this.name = registry.name(data.getName(), container);
		this.description = data.getDescription();

	}

	public GraphUnionType(final GraphRegistry registry, final Class<?> container) {
		this(registry, container, registry.getUnions().extractTypeData(container));
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

	//

	public void addImplementation(GraphObjectType objectType) {
		implementations.add(objectType);
	}

	public List<GraphObjectType> getImplementations() {
		return implementations;
	}

	// builder

	public graphql.schema.GraphQLUnionType.Builder newUnionType(GraphRegistry registry) {

		graphql.schema.GraphQLUnionType.Builder builder = graphql.schema.GraphQLUnionType.newUnionType();

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

	}

}
