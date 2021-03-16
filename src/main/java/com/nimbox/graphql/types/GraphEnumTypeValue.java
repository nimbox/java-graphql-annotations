package com.nimbox.graphql.types;

import static com.nimbox.graphql.utils.IntrospectionUtils.getAnnotationOrThrow;
import static com.nimbox.graphql.utils.IntrospectionUtils.getEnumValueFromField;

import java.lang.reflect.Field;

import com.nimbox.graphql.annotations.GraphQLEnumValue;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.utils.ReservedStrings;

public class GraphEnumTypeValue {

	// properties

	protected final Class<?> enumTypeClass;
	protected final Field enumField;
	protected final Object enumValue;

	protected final String name;
	protected final String description;
	protected final String deprecationReason;

	// constructors

	public GraphEnumTypeValue(final GraphRegistry registry, final Class<?> enumTypeClass, Field enumField) {

		GraphQLEnumValue annotation = getAnnotationOrThrow(GraphQLEnumValue.class, enumField);

		// create

		this.enumTypeClass = enumTypeClass;
		this.enumField = enumField;
		this.enumValue = getEnumValueFromField(enumTypeClass, enumField);

		this.name = annotation.name();
		this.description = ReservedStrings.translate(annotation.description());
		this.deprecationReason = ReservedStrings.translate(annotation.deprecationReason());

	}

	// getters

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getDeprecationReason() {
		return deprecationReason;
	}

	// builder

	public graphql.schema.GraphQLEnumValueDefinition.Builder newValueDefinition(final GraphRegistry registry) {

		graphql.schema.GraphQLEnumValueDefinition.Builder builder = graphql.schema.GraphQLEnumValueDefinition.newEnumValueDefinition();

		builder.name(name);
		if (description != null) {
			builder.description(description);
		}
		if (deprecationReason != null) {
			builder.deprecationReason(deprecationReason);
		}
		builder.value(enumValue);

		return builder;

	}

}
