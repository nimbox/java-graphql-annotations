package com.nimbox.graphql.types;

import java.lang.reflect.Field;
import java.util.Optional;

import com.nimbox.graphql.registries.GraphRegistry;

public class GraphEnumTypeValue {

	// properties

	protected final Class<?> container;
	protected final Field field;

	protected final String name;
	protected final String description;
	protected final String deprecationReason;
	protected final Object value;

	// constructors

	public GraphEnumTypeValue(final GraphRegistry registry, final Class<?> container, final Field field, final Data data) {

		// create

		this.container = container;
		this.field = field;

		this.name = data.getName();
		this.description = data.getDescription();
		this.deprecationReason = data.getDeprecationReason();
		this.value = data.getValue();

	}

	public GraphEnumTypeValue(final GraphRegistry registry, final Class<?> container, final Field field) {
		this(registry, container, field, registry.getEnums().extractTypeFieldData(container, field));
	}

	// getters

	public String getName() {
		return name;
	}

	public Optional<String> getDescription() {
		return Optional.ofNullable(description);
	}

	public Optional<String> getDeprecationReason() {
		return Optional.ofNullable(deprecationReason);
	}

	public Object getValue() {
		return value;
	}

	// builder

	public graphql.schema.GraphQLEnumValueDefinition.Builder newValueDefinition(final GraphRegistry registry) {

		graphql.schema.GraphQLEnumValueDefinition.Builder builder = graphql.schema.GraphQLEnumValueDefinition.newEnumValueDefinition();

		builder.name(getName());
		getDescription().ifPresent(builder::description);
		getDeprecationReason().ifPresent(builder::deprecationReason);
		builder.value(value);

		return builder;

	}

	// data

	public static interface Data {

		public String getName();

		public String getDescription();

		public String getDeprecationReason();

		public Object getValue();

	}

}
