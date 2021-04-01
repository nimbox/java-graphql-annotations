package com.nimbox.graphql.registries;

import static com.nimbox.graphql.utils.IntrospectionUtils.getAnnotationOrThrow;
import static com.nimbox.graphql.utils.IntrospectionUtils.getSuperclassAnnotationOrThrow;
import static graphql.schema.GraphQLTypeReference.typeRef;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.nimbox.graphql.annotations.GraphQLField;
import com.nimbox.graphql.annotations.GraphQLInterface;
import com.nimbox.graphql.types.GraphInterfaceType;
import com.nimbox.graphql.types.GraphInterfaceTypeField;
import com.nimbox.graphql.types.GraphObjectTypeField;
import com.nimbox.graphql.utils.ReservedStringUtils;

import graphql.schema.GraphQLTypeReference;

public class InterfaceTypeRegistry extends GraphTypeFieldRegistry<GraphInterfaceType, Class<?>, Method, GraphInterfaceType.Data, GraphInterfaceTypeField.Data> {

	// constructors

	public InterfaceTypeRegistry(GraphRegistry registry) {
		super(registry);

		withExtractors(new ClassExtractor<>( //
				c -> c.isAnnotationPresent(GraphQLInterface.class), //
				c -> new GraphInterfaceType.Data() {

					GraphQLInterface annotation = getSuperclassAnnotationOrThrow(GraphQLInterface.class, c);

					@Override
					public String getName() {
						return annotation.name();
					}

					@Override
					public String getDescription() {
						return ReservedStringUtils.translate(annotation.description());
					}

					@Override
					public List<String> getOrder() {
						return Arrays.asList(annotation.order());
					}

				} //
		));

		withFieldExtractors(new ClassFieldExtractor<>( //
				(c, m) -> m.isAnnotationPresent(GraphQLField.class), //
				(c, m) -> new GraphObjectTypeField.Data() {

					GraphQLField annotation = getAnnotationOrThrow(GraphQLField.class, m);

					@Override
					public String getName() {
						return annotation.name();
					}

					@Override
					public String getDescription() {
						return ReservedStringUtils.translate(annotation.description());
					}

					@Override
					public String getDeprecationReason() {
						return ReservedStringUtils.translate(annotation.deprecationReason());
					}

				} //
		));

	}

	// methods

	@Override
	public GraphInterfaceType createType(Class<?> container) {
		return new GraphInterfaceType(registry, container);
	}

	@Override
	public GraphQLTypeReference getGraphQLType(Class<?> container) {
		return typeRef(data.get(container).getName());
	}

}
