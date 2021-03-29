package com.nimbox.graphql.types;

import java.lang.reflect.Method;

import com.nimbox.graphql.GraphBuilderException;
import com.nimbox.graphql.annotations.GraphQLInputField;
import com.nimbox.graphql.inputs.GraphInput;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.utils.ReservedStrings;

public class GraphInputObjectTypeField {

	// properties

	private final String name;
	private final String description;
	private final Object defaultValue;

	private final GraphInput returnInput;

	// constructor

	public GraphInputObjectTypeField(final GraphRegistry registry, final Method method) {

		GraphQLInputField annotation = method.getAnnotation(GraphQLInputField.class);
		if (annotation == null) {
			throw new GraphBuilderException(String.format("Expected annotation %s on method %s", GraphQLInputField.class, method));
		}

		this.name = annotation.name();
		this.description = ReservedStrings.translate(annotation.description());
		this.defaultValue = null;

		this.returnInput = GraphInput.of(registry, GraphQLInputField.class, this.name, method);

	}

	// getters

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public GraphInput getReturnInput() {
		return returnInput;
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

		builder.type(returnInput.getGraphQLInputType(registry));

		return builder;

	}

	// object overrides

	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("@GraphQLInputField").append("(").append("name").append(" = ").append(name).append(")");
		builder.append(" ");
		builder.append(returnInput.getDefinition());

		return builder.toString();

	}

}
