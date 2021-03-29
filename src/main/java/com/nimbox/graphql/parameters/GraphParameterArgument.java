package com.nimbox.graphql.parameters;

import java.lang.reflect.Parameter;

import com.nimbox.graphql.annotations.GraphQLArgument;
import com.nimbox.graphql.definitions.GraphInputTypeDefinition;
import com.nimbox.graphql.inputs.GraphInput;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeParameter;
import com.nimbox.graphql.utils.ReservedStrings;

public class GraphParameterArgument extends GraphParameter {

	// properties

	final String name;
	final String description;
	final GraphInput input;

	// constructors

	GraphParameterArgument(final GraphRegistry registry, final Parameter parameter) {

		GraphQLArgument annotation = parameter.getAnnotation(GraphQLArgument.class);
		this.name = annotation.name();
		this.description = ReservedStrings.translate(annotation.description());
		this.input = GraphInput.of(registry, GraphQLArgument.class, this.name, parameter);

	}

	// getters

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public GraphInputTypeDefinition getDefinition() {
		return input.getDefinition();
	}

	@Override
	public RuntimeParameter getRuntimeParameter() {
		return new RuntimeParameter(input.getDefinition(), name);
	}

	// builder

	public graphql.schema.GraphQLArgument.Builder newArgument(GraphRegistry registry) {

		graphql.schema.GraphQLArgument.Builder builder = graphql.schema.GraphQLArgument.newArgument();

		builder.name(name);
		if (this.description != null) {
			builder.description(description);
		}
		builder.type(input.getGraphQLInputType(registry));

		return builder;

	}

	// object overrides

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("@").append(GraphQLArgument.class);
		builder.append("(").append("name").append(" = ").append(name).append(")");
		builder.append(" ");
		builder.append(input.getDefinition());

		return builder.toString();

	}

}
