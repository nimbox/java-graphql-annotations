package com.nimbox.graphql.parameters;

import java.lang.reflect.Parameter;

import com.nimbox.graphgl.contexts.GraphContext;
import com.nimbox.graphql.annotations.GraphQLContext;
import com.nimbox.graphql.definitions.GraphInputTypeDefinition;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeParameter;
import com.nimbox.graphql.utils.ReservedStrings;

public class GraphParameterContext extends GraphParameter {

	// properties

	final String name;
	final GraphContext context;

	// constructors

	GraphParameterContext(final GraphRegistry registry, final Parameter parameter) {

		GraphQLContext annotation = parameter.getAnnotation(GraphQLContext.class);
		this.name = ReservedStrings.translate(annotation.name());
		this.context = GraphContext.of(registry, GraphQLContext.class, this.name, parameter);

	}

	// getters

	public String getName() {
		return name;
	}

	@Override
	public GraphInputTypeDefinition getDefinition() {
		return null;
	}

	@Override
	public RuntimeParameter getRuntimeParameter() {
//		return new RuntimeParameter(inputValue.getInputValue(), name);
		return null;
	}

	// object overrides

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("@").append(GraphQLContext.class);
		if (name != null) {
			builder.append("(").append("name").append(" = ").append(name).append(")");
		}
		builder.append(" ");
		builder.append(context.getDefinition());

		return builder.toString();

	}

}
