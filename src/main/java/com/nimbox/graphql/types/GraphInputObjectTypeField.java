package com.nimbox.graphql.types;

import java.lang.reflect.Method;

import com.nimbox.graphql.GeneratorException;
import com.nimbox.graphql.annotations.GraphQLInputField;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.utils.ReservedStrings;

public class GraphInputObjectTypeField {

	// properties

	private final String name;
	private final String description;
	private final Object defaultValue;

	private final GraphValueClass valueType;

	// constructor

	public GraphInputObjectTypeField(final GraphRegistry registry, final Method method) {

		GraphQLInputField annotation = method.getAnnotation(GraphQLInputField.class);
		if (annotation == null) {
			throw new GeneratorException(String.format("Expected annotation %s on method %s", GraphQLInputField.class, method));
		}

		this.name = annotation.name();
		this.description = ReservedStrings.translate(annotation.description());
		this.defaultValue = null;

		this.valueType = new GraphValueClass(registry, method, method.getGenericReturnType());

	}

	// getters

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public GraphValueClass getValueType() {
		return valueType;
	}

	// builder

	public graphql.schema.GraphQLInputObjectField.Builder newInputObjectField(GraphRegistry registry) {

		graphql.schema.GraphQLInputObjectField.Builder builder = graphql.schema.GraphQLInputObjectField.newInputObjectField();

		builder.name(name);
		if (description != null) {
			builder.description(description);
		}
		if (defaultValue != null) {
			builder.defaultValue(defaultValue);
		}
		builder.type(valueType.getGraphQLInputValueType(registry));

		return builder;

	}

	// object overrides

	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("@GraphQLInputField").append("(").append("name").append(" = ").append(name).append(")");
		builder.append(" ");
		builder.append(valueType);

		return builder.toString();

	}

}
