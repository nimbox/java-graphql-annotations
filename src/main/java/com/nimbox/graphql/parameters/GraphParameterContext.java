package com.nimbox.graphql.parameters;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import com.nimbox.graphql.annotations.GraphQLContext;
import com.nimbox.graphql.definitions.GraphInputTypeDefinition;
import com.nimbox.graphql.nodes.GraphContext;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeParameterContext;
import com.nimbox.graphql.utils.ReservedStringUtils;

public class GraphParameterContext extends GraphParameter {

	// properties

	final String name;
	final GraphContext context;

	// constructors

	GraphParameterContext(final GraphRegistry registry, final Method method, final Parameter parameter) {

		GraphQLContext annotation = parameter.getAnnotation(GraphQLContext.class);

		this.name = ReservedStringUtils.translate(annotation.name());
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
	public RuntimeParameterContext getRuntimeParameter() {
		return new RuntimeParameterContext(name, context.getDefinition());
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
