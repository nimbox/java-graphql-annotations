package com.nimbox.graphql.types;

import static com.nimbox.graphql.utils.IntrospectionUtils.getTypeAnnotationOrThrow;

import com.nimbox.graphql.GeneratorException;
import com.nimbox.graphql.annotations.GraphQLScalar;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.utils.ReservedStrings;

import graphql.schema.Coercing;

public class GraphScalarType {

	// properties

	private final Class<Coercing<?, ?>> scalarTypeClass;

	private final String name;
	private final String description;

	// constructors

	public GraphScalarType(final GraphRegistry registry, final Class<?> scalarTypeClass) {

		GraphQLScalar annotation = getTypeAnnotationOrThrow(GraphQLScalar.class, scalarTypeClass);

		// create

		this.scalarTypeClass = (Class<Coercing<?, ?>>) scalarTypeClass;
		this.name = annotation.name();
		this.description = ReservedStrings.translate(annotation.description());

	}
	// getters

	public Class<?> getScalarTypeClass() {
		return scalarTypeClass;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	// builder

	public graphql.schema.GraphQLScalarType.Builder newScalarType(GraphRegistry registry) {

		try {

			graphql.schema.GraphQLScalarType.Builder builder = graphql.schema.GraphQLScalarType.newScalar();

			builder.name(name);
			if (description != null) {
				builder.description(description);
			}

			builder.coercing(scalarTypeClass.getDeclaredConstructor().newInstance());

			return builder;

		} catch (Exception e) {
			throw new GeneratorException(String.format("Unable to create scalar definition for %s", scalarTypeClass));
		}

	}

}
