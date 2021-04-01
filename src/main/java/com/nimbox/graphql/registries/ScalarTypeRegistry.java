package com.nimbox.graphql.registries;

import static com.nimbox.graphql.utils.IntrospectionUtils.getSuperclassAnnotationOrThrow;
import static graphql.Scalars.GraphQLBoolean;
import static graphql.Scalars.GraphQLFloat;
import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLTypeReference.typeRef;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.nimbox.graphql.annotations.GraphQLScalar;
import com.nimbox.graphql.types.GraphScalarType;
import com.nimbox.graphql.utils.ReservedStringUtils;

import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLTypeReference;

/**
 * Maintains list of scalars by their coercing classes. To get the
 * {@code GraphQLScalarType} the parameter to {@code getGraphQLType} is the
 * output of the coercing class.
 *
 */
public class ScalarTypeRegistry extends GraphTypeRegistry<GraphScalarType, Class<? extends Coercing<?, ?>>, GraphScalarType.Data> {

	// properties

	private Map<Class<?>, GraphQLScalarType> defaults = new HashMap<Class<?>, GraphQLScalarType>();
	private Map<Class<?>, GraphScalarType> dataByReference = new HashMap<Class<?>, GraphScalarType>();

	// constructors

	public ScalarTypeRegistry(GraphRegistry registry) {
		super(registry);

		defaults.put(Integer.class, GraphQLInt);
		defaults.put(Long.class, GraphQLInt);

		defaults.put(Double.class, GraphQLFloat);
		defaults.put(Float.class, GraphQLFloat);
		defaults.put(BigDecimal.class, GraphQLFloat);

		defaults.put(String.class, GraphQLString);
		defaults.put(Boolean.class, GraphQLBoolean);

		registry.name(GraphQLInt.getName(), Integer.class);
		registry.name(GraphQLFloat.getName(), Double.class);
		registry.name(GraphQLString.getName(), String.class);
		registry.name(GraphQLBoolean.getName(), Boolean.class);

		withExtractors(new ClassExtractor<>( //
				c -> c.isAnnotationPresent(GraphQLScalar.class), //
				c -> new GraphScalarType.Data() {

					GraphQLScalar annotation = getSuperclassAnnotationOrThrow(GraphQLScalar.class, c);

					@Override
					public Class<?> getType() {
						return annotation.type();
					}

					@Override
					public String getName() {
						return annotation.name();
					}

					@Override
					public String getDescription() {
						return ReservedStringUtils.translate(annotation.description());
					}

				} //
		));

	}

	// methods

	@Override
	public GraphScalarType createType(Class<? extends Coercing<?, ?>> container) {

		GraphScalarType type = new GraphScalarType(registry, container);
		dataByReference.put(type.getReferenceContainer(), type);

		return type;

	}

	@Override
	public GraphQLTypeReference getGraphQLType(Class<? extends Coercing<?, ?>> container) {
		return typeRef(data.get(container).getName());
	}

	// methods on reference container

	public boolean containsType(Class<?> referenceContainer) {
		return defaults.containsKey(referenceContainer) || dataByReference.containsKey(referenceContainer);
	}

	public GraphQLScalarType getGraphQLTypeType(Class<?> referenceContainer) {

		if (defaults.containsKey(referenceContainer)) {
			return defaults.get(referenceContainer);
		}

		return dataByReference.get(referenceContainer).built();

	}

}
