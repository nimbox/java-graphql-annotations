package com.nimbox.graphql.types;

import static com.nimbox.graphql.utils.IntrospectionUtils.getAnnotationOrThrow;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import com.nimbox.graphql.annotations.GraphQLQuery;
import com.nimbox.graphql.annotations.GraphQLType;
import com.nimbox.graphql.parameters.GraphParameter;
import com.nimbox.graphql.parameters.GraphParameterArgument;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeParameterFactory;
import com.nimbox.graphql.utils.ReservedStrings;

import graphql.schema.DataFetcher;

public abstract class GraphField {

	// properties

	protected final Class<?> typeClass;
	protected final Method fieldMethod;

	protected final String name;
	protected final String description;
	protected final String deprecationReason;

	protected final GraphValueClass valueClass;
	protected final List<GraphParameter> parameters = new ArrayList<GraphParameter>();

	// constructor

	public GraphField(final GraphRegistry registry, final Class<?> typeClass, final Method fieldMethod) {

		GraphQLQuery annotation = getAnnotationOrThrow(GraphQLQuery.class, fieldMethod);

		// create

		this.typeClass = typeClass;
		this.fieldMethod = fieldMethod;

		this.name = annotation.name();
		this.description = ReservedStrings.translate(annotation.description());
		this.deprecationReason = ReservedStrings.translate(annotation.deprecationReason());

		this.valueClass = new GraphValueClass(registry, fieldMethod, fieldMethod.getGenericReturnType());
		if (this.valueClass.getValueClass().isAnnotationPresent(GraphQLType.class)) {
			registry.getObjects().of(this.valueClass.getValueClass());
		}

		// parameters

		for (Parameter p : fieldMethod.getParameters()) {
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

		builder.append("@").append(GraphQLQuery.class.getSimpleName()).append("(").append("name").append(" = ").append(name).append(")");
		builder.append(" ");
		builder.append(valueClass);

		return builder.toString();

	}

}
