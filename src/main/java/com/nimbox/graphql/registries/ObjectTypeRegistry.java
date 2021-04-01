package com.nimbox.graphql.registries;

import static com.nimbox.graphql.utils.IntrospectionUtils.getAnnotationOrThrow;
import static com.nimbox.graphql.utils.IntrospectionUtils.getSuperclassAnnotationOrThrow;
import static graphql.schema.GraphQLTypeReference.typeRef;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import com.nimbox.graphql.annotations.GraphQLField;
import com.nimbox.graphql.annotations.GraphQLType;
import com.nimbox.graphql.types.GraphObjectType;
import com.nimbox.graphql.types.GraphObjectTypeField;
import com.nimbox.graphql.utils.ReservedStringUtils;

import graphql.schema.GraphQLTypeReference;

public class ObjectTypeRegistry extends GraphTypeFieldRegistry<GraphObjectType, Class<?>, Method, GraphObjectType.Data, GraphObjectTypeField.Data> {

	// constructors

	public ObjectTypeRegistry(GraphRegistry registry) {
		super(registry);

		withExtractors(new ClassExtractor<>( //
				c -> !Modifier.isAbstract(c.getModifiers()) && c.isAnnotationPresent(GraphQLType.class), //
				c -> new GraphObjectType.Data() {

					GraphQLType annotation = getSuperclassAnnotationOrThrow(GraphQLType.class, c);

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
	public GraphObjectType createType(Class<?> container) {
		return new GraphObjectType(registry, container);
	}

	@Override
	public GraphQLTypeReference getGraphQLType(Class<?> container) {
		return typeRef(data.get(container).getName());
	}

}
