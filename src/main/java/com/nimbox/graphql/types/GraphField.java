package com.nimbox.graphql.types;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import com.nimbox.graphql.annotations.GraphQLField;
import com.nimbox.graphql.outputs.GraphOutput;
import com.nimbox.graphql.parameters.GraphParameter;
import com.nimbox.graphql.parameters.GraphParameterArgument;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeParameterFactory;

import graphql.schema.DataFetcher;

public abstract class GraphField {

	// properties

	protected final Class<?> container;
	protected final Method method;

	protected final String name;
	protected final String description;
	protected final String deprecationReason;

	protected final GraphOutput definition;
	protected final List<GraphParameter> parameters = new ArrayList<GraphParameter>();

	// constructor

	GraphField(final GraphRegistry registry, final TypeAnnotation definition, final Class<?> container, final Method method) {

		// create

		this.container = container;
		this.method = method;

		this.name = definition.getName();
		this.description = definition.getDescription();
		this.deprecationReason = definition.getDeprecationReason();

		this.definition = GraphOutput.of(registry, null, this.name, method);

//		if (this.output..getType().isAnnotationPresent(GraphQLType.class)) {
//			registry.getObjects().of(this.valueClass.getType());
//		}

		// parameters

		for (Parameter p : method.getParameters()) {
			this.parameters.add(GraphParameter.of(registry, p));
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

	public GraphOutput getReturn() {
		return definition;
	}

	// methods

	public graphql.schema.GraphQLFieldDefinition.Builder newFieldDefinition(final GraphRegistry registry) {

		graphql.schema.GraphQLFieldDefinition.Builder builder = graphql.schema.GraphQLFieldDefinition.newFieldDefinition();

		builder.name(name);
		if (description != null) {
			builder.description(description);
		}
		if (deprecationReason != null) {
			builder.deprecate(deprecationReason);
		}
		builder.type(definition.getGraphQLOutputType(registry));

		for (GraphParameter parameter : parameters) {
			if (parameter instanceof GraphParameterArgument) {
				builder.argument(((GraphParameterArgument) parameter).newArgument(registry));
			}
		}

		return builder;

	}

	public abstract DataFetcher<?> getFetcher(RuntimeParameterFactory factory);

	// object overrides

	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("@").append(GraphQLField.class.getSimpleName()).append("(").append("name").append(" = ").append(name).append(")");
		builder.append(" ");
		builder.append(definition.getOutputType());

		return builder.toString();

	}

	// classes

	public static interface TypeAnnotation {

		public String getName();

		public String getDescription();

		public String getDeprecationReason();

	}

}
