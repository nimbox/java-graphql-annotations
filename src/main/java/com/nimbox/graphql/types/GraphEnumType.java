package com.nimbox.graphql.types;

import static com.nimbox.graphql.utils.IntrospectionUtils.getTypeAnnotationOrThrow;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nimbox.graphql.annotations.GraphQLEnum;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.utils.ReservedStrings;

public class GraphEnumType {

	// properties

	private final Class<?> enumTypeClass;

	private final String name;
	private final String description;
	private final List<String> valueOrder;

	private final Map<Field, GraphEnumTypeValue> values = new LinkedHashMap<Field, GraphEnumTypeValue>();

	// constructors

	public GraphEnumType(final GraphRegistry registry, final Class<?> enumTypeClass) {

		GraphQLEnum annotation = getTypeAnnotationOrThrow(GraphQLEnum.class, enumTypeClass);

		this.enumTypeClass = enumTypeClass;

		this.name = annotation.name();
		this.description = ReservedStrings.translate(annotation.description());
		this.valueOrder = Arrays.asList(annotation.valueOrder());

		// values

		for (Field field : enumTypeClass.getFields()) {
			values.put(field, new GraphEnumTypeValue(registry, enumTypeClass, field));
		}

	}

	// getters

	public Class<?> getEnumTypeClass() {
		return enumTypeClass;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public List<String> getValueOrder() {
		return valueOrder;
	}

	public Map<Field, GraphEnumTypeValue> getValues() {
		return values;
	}

	// builder

	public graphql.schema.GraphQLEnumType.Builder newEnumType(GraphRegistry registry) {

		graphql.schema.GraphQLEnumType.Builder builder = graphql.schema.GraphQLEnumType.newEnum();

		builder.name(name);
		if (description != null) {
			builder.description(description);
		}

		return builder;

	}

}
