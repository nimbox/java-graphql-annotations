package com.nimbox.graphql.types;

import java.util.Optional;

import com.nimbox.graphql.GraphBuilderException;
import com.nimbox.graphql.registries.GraphRegistry;

import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

public class GraphScalarType {

	// properties

	private final Class<? extends Coercing<?, ?>> container;
	private final Class<?> referenceContainer;

	private final String name;
	private final String description;

	private final GraphQLScalarType built;

	// constructors

	public GraphScalarType(final GraphRegistry registry, final Class<? extends Coercing<?, ?>> container, Data data) {

		// create

		this.container = container;
		this.referenceContainer = data.getType();

		this.name = registry.name(data.getName(), container);
		this.description = data.getDescription();

		// build

		this.built = newScalarType(registry).build();

	}

	public GraphScalarType(final GraphRegistry registry, final Class<? extends Coercing<?, ?>> container) {
		this(registry, container, registry.getScalars().extractTypeData(container));
	}

	// getters

	public Class<? extends Coercing<?, ?>> getContainer() {
		return container;
	}

	public Class<?> getReferenceContainer() {
		return referenceContainer;
	}

	public String getName() {
		return name;
	}

	public Optional<String> getDescription() {
		return Optional.of(description);
	}

	public GraphQLScalarType built() {
		return built;
	}

	// builder

	private graphql.schema.GraphQLScalarType.Builder newScalarType(GraphRegistry registry) {

		try {

			graphql.schema.GraphQLScalarType.Builder builder = graphql.schema.GraphQLScalarType.newScalar();

			builder.name(getName());
			getDescription().ifPresent(builder::description);
			builder.coercing(container.getDeclaredConstructor().newInstance());

			return builder;

		} catch (Exception e) {
			throw new GraphBuilderException(String.format("Unable to create scalar definition for %s", container));
		}

	}

	// data

	public static interface Data {

		Class<?> getType();

		String getName();

		String getDescription();

	}

}
