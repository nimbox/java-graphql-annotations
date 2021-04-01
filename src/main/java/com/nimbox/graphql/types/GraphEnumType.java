package com.nimbox.graphql.types;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.nimbox.graphql.registries.GraphRegistry;

public class GraphEnumType {

	// properties

	private final Class<?> container;

	private final String name;
	private final String description;
	private final List<String> order;

	private final Map<Field, GraphEnumTypeValue> values = new LinkedHashMap<Field, GraphEnumTypeValue>();

	// constructors

	public GraphEnumType(final GraphRegistry registry, final Class<?> container, final Data data) {

		// create

		this.container = container;

		this.name = registry.name(data.getName(), container);
		this.description = data.getDescription();
		this.order = data.getOrder();

		// fields

		for (Field field : container.getFields()) {
			if (registry.getEnums().acceptTypeField(container, field)) {
				values.put(field, new GraphEnumTypeValue(registry, container, field));
			}
		}

	}

	public GraphEnumType(final GraphRegistry registry, final Class<?> container) {
		this(registry, container, registry.getEnums().extractTypeData(container));
	}

	// getters

	public Class<?> getContainer() {
		return container;
	}

	public String getName() {
		return name;
	}

	public Optional<String> getDescription() {
		return Optional.of(description);
	}

	public List<String> getOrder() {
		return order;
	}

	public Map<Field, GraphEnumTypeValue> getValues() {
		return values;
	}

	// builder

	public graphql.schema.GraphQLEnumType.Builder newEnumType(final GraphRegistry registry) {

		graphql.schema.GraphQLEnumType.Builder builder = graphql.schema.GraphQLEnumType.newEnum();

		builder.name(getName());
		getDescription().ifPresent(builder::description);

		return builder;

	}

	// data

	public static interface Data {

		String getName();

		String getDescription();

		List<String> getOrder();

	}

}
