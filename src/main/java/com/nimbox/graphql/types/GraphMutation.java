package com.nimbox.graphql.types;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import com.nimbox.graphql.GeneratorException;
import com.nimbox.graphql.annotations.GraphQLMutation;
import com.nimbox.graphql.annotations.GraphQLType;
import com.nimbox.graphql.parameters.GraphParameter;
import com.nimbox.graphql.parameters.GraphParameterArgument;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.utils.ReservedStrings;

public class GraphMutation {

	// properties

	private final String name;
	private final String description;
	private final String deprecationReason;

	private final GraphValueClass valueType;
	private final List<GraphParameter> parameters = new ArrayList<GraphParameter>();

	// constructor

	public GraphMutation(final GraphRegistry registry, final Method method) {

		GraphQLMutation annotation = method.getAnnotation(GraphQLMutation.class);
		if (annotation == null) {
			throw new GeneratorException(String.format("Method %s does not have annotation %s", method, GraphQLMutation.class));
		}

		// create

		this.name = annotation.name();
		this.description = ReservedStrings.translate(annotation.description());
		this.deprecationReason = ReservedStrings.translate(annotation.deprecationReason());

		this.valueType = new GraphValueClass(registry, method, method.getGenericReturnType());

		// TODO this is probably not the place
		if (this.valueType.getValueClass().isAnnotationPresent(GraphQLType.class)) {
			registry.getObjects().of(this.valueType.getValueClass());
		}

		for (Parameter fieldParameter : method.getParameters()) {
			this.parameters.add(GraphParameter.of(registry, fieldParameter));
		}

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

	public GraphValueClass getReturnType() {
		return valueType;
	}

	// methods

	public graphql.schema.GraphQLFieldDefinition.Builder newFieldDefinition(GraphRegistry registry) {

		graphql.schema.GraphQLFieldDefinition.Builder builder = graphql.schema.GraphQLFieldDefinition.newFieldDefinition();

		builder.name(name);
		if (description != null) {
			builder.description(description);
		}
		if (deprecationReason != null) {
			builder.deprecate(deprecationReason);
		}
		builder.type(valueType.getGraphQLOutputValueType(registry));

		for (GraphParameter parameter : parameters) {
			if (parameter instanceof GraphParameterArgument) {
				builder.argument(((GraphParameterArgument) parameter).newArgument(registry));
			}
		}

		return builder;

	}

	// object overrides

	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("@GraphQLMutation").append("(").append("name").append(" = ").append(name).append(")");
		builder.append(" ");
		builder.append(valueType);

		return builder.toString();

	}

}
