package com.nimbox.graphql.types;

import static com.nimbox.graphql.utils.IntrospectionUtils.getTypeAnnotationOrThrow;

import com.nimbox.graphql.GraphBuilderException;
import com.nimbox.graphql.annotations.GraphQLScalar;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.utils.ReservedStrings;

import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

public class GraphScalarType {

	// properties

	private final Class<?> javaClass;
	private final Class<Coercing<?, ?>> scalarCoercingClass;

	private final String name;
	private final String description;

	private final GraphQLScalarType built;

	// constructors

	public GraphScalarType(final GraphRegistry registry, final Class<?> javaClass, final Class<?> scalarCoercingClass) {

		GraphQLScalar annotation = getTypeAnnotationOrThrow(GraphQLScalar.class, scalarCoercingClass);

		// create

		this.javaClass = javaClass;
		this.scalarCoercingClass = (Class<Coercing<?, ?>>) scalarCoercingClass;

		this.name = annotation.name();
		this.description = ReservedStrings.translate(annotation.description());

		this.built = newScalarType(registry).build();

	}
	// getters

	public Class<?> getJavaClass() {
		return javaClass;
	}

	public Class<Coercing<?, ?>> getScalarCoercingClass() {
		return scalarCoercingClass;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public GraphQLScalarType built() {
		return built;
	}

	// builder

	private graphql.schema.GraphQLScalarType.Builder newScalarType(GraphRegistry registry) {

		try {

			graphql.schema.GraphQLScalarType.Builder builder = graphql.schema.GraphQLScalarType.newScalar();

			builder.name(name);
			if (description != null) {
				builder.description(description);
			}

			builder.coercing(scalarCoercingClass.getDeclaredConstructor().newInstance());

			return builder;

		} catch (Exception e) {
			throw new GraphBuilderException(String.format("Unable to create scalar definition for %s", scalarCoercingClass));
		}

	}

}
