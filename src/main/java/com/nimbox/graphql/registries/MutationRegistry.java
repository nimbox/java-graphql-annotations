package com.nimbox.graphql.registries;

import static com.nimbox.graphql.utils.IntrospectionUtils.getAnnotationOrThrow;

import java.lang.reflect.Method;

import com.nimbox.graphql.annotations.GraphQLMutation;
import com.nimbox.graphql.types.GraphField;
import com.nimbox.graphql.types.GraphMutationField;
import com.nimbox.graphql.utils.ReservedStringUtils;

public class MutationRegistry extends OperationRegistry<GraphMutationField> {

	// constructors

	public MutationRegistry(GraphRegistry registry) {
		super(registry);

		withFieldExtractors(new ClassFieldExtractor<>( //
				(c, m) -> m.isAnnotationPresent(GraphQLMutation.class), //
				(c, m) -> new GraphField.Data() {

					GraphQLMutation annotation = getAnnotationOrThrow(GraphQLMutation.class, m);

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

	public GraphMutationField createType(final Class<?> container, Method method) {
		return new GraphMutationField(registry, container, method);
	}

}
