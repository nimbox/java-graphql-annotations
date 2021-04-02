package com.nimbox.graphql.registries;

import static com.nimbox.graphql.utils.IntrospectionUtils.getAnnotationOrThrow;
import static com.nimbox.graphql.utils.IntrospectionUtils.getEnumValueFromField;
import static com.nimbox.graphql.utils.IntrospectionUtils.getSuperclassAnnotationOrThrow;
import static graphql.schema.GraphQLTypeReference.typeRef;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.nimbox.graphql.annotations.GraphQLEnum;
import com.nimbox.graphql.annotations.GraphQLEnumValue;
import com.nimbox.graphql.types.GraphEnumType;
import com.nimbox.graphql.types.GraphEnumTypeValue;
import com.nimbox.graphql.utils.ReservedStringUtils;

import graphql.schema.GraphQLTypeReference;

public class EnumTypeRegistry extends TypeFieldRegistry<GraphEnumType, Class<?>, Field, GraphEnumType.Data, GraphEnumTypeValue.Data> {

	// constructors

	public EnumTypeRegistry(GraphRegistry registry) {
		super(registry);

		withExtractors(new ClassExtractor<>( //
				c -> c.isAnnotationPresent(GraphQLEnum.class), //
				c -> new GraphEnumType.Data() {

					GraphQLEnum annotation = getSuperclassAnnotationOrThrow(GraphQLEnum.class, c);

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
				(c, f) -> f.isAnnotationPresent(GraphQLEnumValue.class), //
				(c, f) -> new GraphEnumTypeValue.Data() {

					GraphQLEnumValue annotation = getAnnotationOrThrow(GraphQLEnumValue.class, f);

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

					@Override
					public Object getValue() {
						return getEnumValueFromField(c, f);
					}

				} //
		));

	}

	// methods

	@Override
	public GraphEnumType createType(Class<?> container) {
		return new GraphEnumType(registry, container);
	}

	@Override
	public GraphQLTypeReference getGraphQLType(Class<?> container) {
		return typeRef(data.get(container).getName());
	}

}
