package com.nimbox.graphql.types;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.nimbox.graphql.annotations.GraphQLField;
import com.nimbox.graphql.nodes.GraphOutput;
import com.nimbox.graphql.parameters.GraphParameter;
import com.nimbox.graphql.parameters.GraphParameterArgument;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeParameterFactory;

import graphql.schema.DataFetcher;

public abstract class GraphField {

	// properties

	final Class<?> container;
	final Method method;

	protected final String name;
	protected final String description;
	protected final String deprecationReason;

	protected final GraphOutput definition;
	protected final List<GraphParameter> parameters = new ArrayList<GraphParameter>();

	// constructor

	GraphField(final GraphRegistry registry, final Class<?> container, final Method method, final Data data) {

		// create

		this.container = container;
		this.method = method;

		this.name = data.getName();
		this.description = data.getDescription();
		this.deprecationReason = data.getDeprecationReason();

		this.definition = GraphOutput.of(registry, this.name, method);

		// parameters

		for (Parameter parameter : method.getParameters()) {
			this.parameters.add(GraphParameter.of(registry, method, parameter));
		}

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

	public Optional<String> getDeprecationReason() {
		return Optional.ofNullable(deprecationReason);
	}

	public GraphOutput getReturn() {
		return definition;
	}

	// methods

	public graphql.schema.GraphQLFieldDefinition.Builder newFieldDefinition(final GraphRegistry registry) {

		graphql.schema.GraphQLFieldDefinition.Builder builder = graphql.schema.GraphQLFieldDefinition.newFieldDefinition();

		builder.name(name);
		getDescription().ifPresent(builder::description);
		getDeprecationReason().ifPresent(builder::deprecate);
		builder.type(definition.getGraphQLOutputType(registry));

		for (GraphParameter parameter : parameters) {
			if (parameter instanceof GraphParameterArgument) {
				builder.argument(((GraphParameterArgument) parameter).newArgument(registry));
			}
		}

		return builder;

	}

	public abstract DataFetcher<?> getDataFetcher(RuntimeParameterFactory factory);

	// object overrides

	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("@").append(GraphQLField.class.getSimpleName()).append("(").append("name").append(" = ").append(name).append(")");
		builder.append(" ");
		builder.append(definition.getDefinition());

		return builder.toString();

	}

	// data

	public static interface Data {

		public String getName();

		public String getDescription();

		public String getDeprecationReason();

	}

}
