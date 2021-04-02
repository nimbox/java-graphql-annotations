package com.nimbox.graphql.registries;

import static com.nimbox.graphql.utils.IntrospectionUtils.getAnnotationOrThrow;
import static com.nimbox.graphql.utils.IntrospectionUtils.getSuperclassAnnotationOrThrow;
import static graphql.schema.GraphQLTypeReference.typeRef;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.nimbox.graphql.annotations.GraphQLInput;
import com.nimbox.graphql.annotations.GraphQLInputField;
import com.nimbox.graphql.types.GraphInputObjectType;
import com.nimbox.graphql.types.GraphInputObjectTypeField;
import com.nimbox.graphql.utils.ReservedStringUtils;

import graphql.schema.GraphQLTypeReference;

public class InputObjectTypeRegistry extends TypeFieldRegistry<GraphInputObjectType, Class<?>, Method, GraphInputObjectType.Data, GraphInputObjectTypeField.Data> {

	// constructors

	public InputObjectTypeRegistry(GraphRegistry registry) {
		super(registry);

		withExtractors(new ClassExtractor<>( //
				c -> c.isAnnotationPresent(GraphQLInput.class), //
				c -> new GraphInputObjectType.Data() {

					GraphQLInput annotation = getSuperclassAnnotationOrThrow(GraphQLInput.class, c);

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
				(c, m) -> m.isAnnotationPresent(GraphQLInputField.class), //
				(c, m) -> new GraphInputObjectTypeField.Data() {

					GraphQLInputField annotation = getAnnotationOrThrow(GraphQLInputField.class, m);

					@Override
					public String getName() {
						return annotation.name();
					}

					@Override
					public String getDescription() {
						return ReservedStringUtils.translate(annotation.description());
					}

					@Override
					public Object getDefaultValue() {
						return null;
					}

				} //
		));

	}

	// methods

	@Override
	public GraphInputObjectType createType(Class<?> container) {
		return new GraphInputObjectType(registry, container);
	}

	@Override
	public GraphQLTypeReference getGraphQLType(Class<?> container) {
		return typeRef(data.get(container).getName());
	}

}
