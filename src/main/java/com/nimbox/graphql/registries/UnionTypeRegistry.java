package com.nimbox.graphql.registries;

import static com.nimbox.graphql.utils.IntrospectionUtils.getSuperclassAnnotationOrThrow;
import static graphql.schema.GraphQLTypeReference.typeRef;

import com.nimbox.graphql.annotations.GraphQLUnion;
import com.nimbox.graphql.types.GraphUnionType;
import com.nimbox.graphql.utils.ReservedStringUtils;

import graphql.schema.GraphQLTypeReference;

public class UnionTypeRegistry extends TypeRegistry<GraphUnionType, Class<?>, GraphUnionType.Data> {

	// constructors

	public UnionTypeRegistry(GraphRegistry registry) {
		super(registry);

		withExtractors(new ClassExtractor<>( //
				c -> c.isAnnotationPresent(GraphQLUnion.class), //
				c -> new GraphUnionType.Data() {

					GraphQLUnion annotation = getSuperclassAnnotationOrThrow(GraphQLUnion.class, c);

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
	public GraphUnionType createType(Class<?> container) {
		return new GraphUnionType(registry, container);
	}

	@Override
	public GraphQLTypeReference getGraphQLType(Class<?> container) {
		return typeRef(data.get(container).getName());
	}

}
