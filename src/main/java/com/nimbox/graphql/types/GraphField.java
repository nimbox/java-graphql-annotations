package com.nimbox.graphql.types;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import com.nimbox.graphql.annotations.GraphQLField;
import com.nimbox.graphql.annotations.GraphQLType;
import com.nimbox.graphql.parameters.GraphParameter;
import com.nimbox.graphql.parameters.GraphParameterArgument;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeParameterFactory;

import graphql.schema.DataFetcher;

public abstract class GraphField {

	// properties

	protected final Class<?> typeClass;
	protected final Method typeMethod;

	protected final String name;
	protected final String description;
	protected final String deprecationReason;

	protected final GraphValueClass valueClass;
	protected final List<GraphParameter> parameters = new ArrayList<GraphParameter>();

	// constructor

	public GraphField(final GraphRegistry registry, final Definition definition, final Class<?> typeClass, final Method typeMethod) {

		// create

		this.typeClass = typeClass;
		this.typeMethod = typeMethod;

		this.name = definition.getName();
		this.description = definition.getDescription();
		this.deprecationReason = definition.getDeprecationReason();

		this.valueClass = new GraphValueClass(registry, typeMethod, typeMethod.getGenericReturnType());
		if (this.valueClass.getValueClass().isAnnotationPresent(GraphQLType.class)) {
			registry.getObjects().of(this.valueClass.getValueClass());
		}

		// parameters

		for (Parameter p : typeMethod.getParameters()) {
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

	public GraphValueClass getValueClass() {
		return valueClass;
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
		builder.type(valueClass.getGraphQLOutputValueType(registry));

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
		builder.append(valueClass);

		return builder.toString();

	}

	// classes

	public static interface Definition {

		public String getName();

		public String getDescription();

		public String getDeprecationReason();

	}

}
