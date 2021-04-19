package com.nimbox.graphql.registries;

import static com.nimbox.graphql.utils.IntrospectionUtils.getAnnotationOrThrow;
import static com.nimbox.graphql.utils.IntrospectionUtils.getSuperclassAnnotationOrThrow;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nimbox.graphql.annotations.GraphQLExtension;
import com.nimbox.graphql.annotations.GraphQLField;
import com.nimbox.graphql.types.GraphField;
import com.nimbox.graphql.types.GraphObjectTypeExtension;
import com.nimbox.graphql.types.GraphObjectTypeField;
import com.nimbox.graphql.types.GraphTypeExtension;
import com.nimbox.graphql.utils.ReservedStringUtils;

import graphql.schema.GraphQLTypeReference;

abstract class TypeExtensionRegistry<T> extends TypeFieldRegistry<T, Class<?>, Method, GraphTypeExtension.Data, GraphField.Data> {

	// properties

	final Map<Class<?>, List<T>> dataByReference = new HashMap<Class<?>, List<T>>();

	// constructors

	TypeExtensionRegistry(final GraphRegistry registry) {
		super(registry);

		withExtractors(new ClassExtractor<>( //
				c -> c.isAnnotationPresent(GraphQLExtension.class), //
				c -> new GraphObjectTypeExtension.Data() {

					GraphQLExtension annotation = getSuperclassAnnotationOrThrow(GraphQLExtension.class, c);

					@Override
					public Class<?> getType() {
						return annotation.type();
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

	public abstract boolean isAcceptable(Class<?> container);

	@Override
	public GraphQLTypeReference getGraphQLType(final Class<?> container) {
		throw new UnsupportedOperationException();
	}

	// methods on reference container

	public List<T> getForType(final Class<?> referenceContainer) {
		return dataByReference.getOrDefault(referenceContainer, Collections.emptyList());
	}

}
