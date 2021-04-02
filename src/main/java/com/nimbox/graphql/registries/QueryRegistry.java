package com.nimbox.graphql.registries;

import static com.nimbox.graphql.utils.IntrospectionUtils.getAnnotationOrThrow;

import java.lang.reflect.Method;

import com.nimbox.graphql.annotations.GraphQLQuery;
import com.nimbox.graphql.types.GraphField;
import com.nimbox.graphql.types.GraphQueryField;
import com.nimbox.graphql.utils.ReservedStringUtils;

public class QueryRegistry extends OperationRegistry<GraphQueryField> {

	// constructors

	public QueryRegistry(final GraphRegistry registry) {
		super(registry);

		withFieldExtractors(new ClassFieldExtractor<>( //
				(c, m) -> m.isAnnotationPresent(GraphQLQuery.class), //
				(c, m) -> new GraphField.Data() {

					GraphQLQuery annotation = getAnnotationOrThrow(GraphQLQuery.class, m);

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

	public GraphQueryField createType(final Class<?> container, final Method method) {
		return new GraphQueryField(registry, container, method);
	}

}
