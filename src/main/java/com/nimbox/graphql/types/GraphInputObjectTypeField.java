package com.nimbox.graphql.types;

import java.lang.reflect.Method;
import java.util.Optional;

import com.nimbox.graphql.annotations.GraphQLInputField;
import com.nimbox.graphql.nodes.GraphInput;
import com.nimbox.graphql.registries.GraphRegistry;

public class GraphInputObjectTypeField {

	// properties

	final Class<?> container;
	final Method method;

	private final String name;
	private final String description;
	private final Object defaultValue;

	private final GraphInput definition;

	// constructor

	public GraphInputObjectTypeField(final GraphRegistry registry, final Class<?> container, final Method method, final Data data) {

		// create

		this.container = container;
		this.method = method;

		this.name = data.getName();
		this.description = data.getDescription();
		this.defaultValue = data.getDefaultValue();

		this.definition = GraphInput.of(registry, this.name, method);

	}

	public GraphInputObjectTypeField(final GraphRegistry registry, final Class<?> container, final Method method) {
		this(registry, container, method, registry.getInputObjects().extractTypeFieldData(container, method));
	}

	// getters

	public Class<?> getContainer() {
		return container;
	}

	public Method getMethod() {
		return method;
	}

	public String getName() {
		return name;
	}

	public Optional<String> getDescription() {
		return Optional.ofNullable(description);
	}

	public Optional<Object> getDefaultValue() {
		return Optional.ofNullable(defaultValue);
	}

	public GraphInput getReturnInput() {
		return definition;
	}

	// builder

	public graphql.schema.GraphQLInputObjectField.Builder newInputObjectField(GraphRegistry registry) {

		graphql.schema.GraphQLInputObjectField.Builder builder = graphql.schema.GraphQLInputObjectField.newInputObjectField();

		builder.name(getName());
		getDescription().ifPresent(builder::description);
		getDefaultValue().ifPresent(builder::defaultValue);
		builder.type(definition.getGraphQLInputType(registry));

		return builder;

	}

	// object overrides

	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("@").append(GraphQLInputField.class.getSimpleName()).append("(").append("name").append(" = ").append(name).append(")");
		builder.append(" ");
		builder.append(definition.getDefinition());

		return builder.toString();

	}

	// data

	public static interface Data {

		public String getName();

		public String getDescription();

		public Object getDefaultValue();

	}

}
