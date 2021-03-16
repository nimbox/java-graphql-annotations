package com.nimbox.graphql.parameters;

import java.lang.reflect.Parameter;

import com.nimbox.graphql.GeneratorException;
import com.nimbox.graphql.annotations.GraphQLArgument;
import com.nimbox.graphql.annotations.GraphQLEnum;
import com.nimbox.graphql.annotations.GraphQLInput;
import com.nimbox.graphql.parameters.arguments.ArgumentFactory;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeNamedParameter;
import com.nimbox.graphql.types.GraphValueClass;
import com.nimbox.graphql.utils.ReservedStrings;

import graphql.schema.DataFetchingEnvironment;

public class GraphParameterArgument extends GraphParameter {

	// properties

	protected final String name;
	protected final String description;

	private final ArgumentFactory factory;

	// constructors

	public GraphParameterArgument(final GraphRegistry registry, final Parameter parameter, final GraphValueClass valueType) {
		super(registry, parameter, valueType, GraphQLArgument.class);

		GraphQLArgument annotation = parameter.getAnnotation(GraphQLArgument.class);

		this.name = annotation.name();
		this.description = ReservedStrings.isDefined(annotation.description()) ? annotation.description() : null;

		this.factory = registry.getArgumentFactory();

	}

	public static GraphParameterArgument of(final GraphRegistry registry, final Parameter fieldParameter) {

		GraphValueClass valueType = new GraphValueClass(registry, fieldParameter, fieldParameter.getParameterizedType());

		if (registry.getScalars().contains(valueType.getValueClass())) {
			return new GraphParameterArgumentScalar(registry, fieldParameter, valueType);
		}

		if (valueType.getValueClass().isAnnotationPresent(GraphQLInput.class)) {
			registry.getInputObjects().of(valueType.getValueClass());
			return new GraphParameterArgumentInput(registry, fieldParameter, valueType);
		}

		if (valueType.getValueClass().isAnnotationPresent(GraphQLEnum.class)) {
			registry.getEnums().of(valueType.getValueClass());
			return new GraphParameterArgumentEnum(registry, fieldParameter, valueType);
		}

		throw new GeneratorException(String.format("Parameter %s annotated with %s does not have a recognized return type %s", //
				fieldParameter.getName(), //
				GraphQLArgument.class, //
				valueType.getValueClass()) //
		);

	}

	// getters

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public <T> T getParameterValue(DataFetchingEnvironment environment) throws Exception {
		return factory.of(valueClass, environment.getArguments(), name);
	}

	@Override
	public RuntimeNamedParameter getRuntimeParameter() {
		return new RuntimeNamedParameter(valueClass, name);
	}

	// methods

	public graphql.schema.GraphQLArgument.Builder newArgument(GraphRegistry registry) {

		graphql.schema.GraphQLArgument.Builder builder = graphql.schema.GraphQLArgument.newArgument();

		builder.name(name);
		if (this.description != null) {
			builder.description(description);
		}
		builder.type(valueClass.getGraphQLInputValueType(registry));

		return builder;

	}

	// object overrides

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("@").append(GraphQLArgument.class);
		builder.append("(").append("name").append(" = ").append(name).append(")");
		builder.append(" ");
		builder.append(valueClass.getValueClass());

		return builder.toString();

	}

}
