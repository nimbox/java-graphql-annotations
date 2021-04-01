package com.nimbox.graphql.registries;

import static com.nimbox.graphql.utils.IntrospectionUtils.getAnnotationOrThrow;
import static com.nimbox.graphql.utils.IntrospectionUtils.getSuperclassAnnotationOrThrow;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nimbox.graphql.annotations.GraphQLField;
import com.nimbox.graphql.annotations.GraphQLTypeExtension;
import com.nimbox.graphql.types.GraphObjectTypeExtension;
import com.nimbox.graphql.types.GraphObjectTypeField;
import com.nimbox.graphql.utils.ReservedStringUtils;

import graphql.schema.GraphQLTypeReference;

public class ObjectTypeExtensionRegistry extends GraphTypeFieldRegistry<GraphObjectTypeExtension, Class<?>, Method, GraphObjectTypeExtension.Data, GraphObjectTypeField.Data> {

	// properties

	private Map<Class<?>, List<GraphObjectTypeExtension>> dataByReference = new HashMap<Class<?>, List<GraphObjectTypeExtension>>();

	// constructors

	public ObjectTypeExtensionRegistry(GraphRegistry registry) {
		super(registry);

		withExtractors(new ClassExtractor<>( //
				c -> !Modifier.isAbstract(c.getModifiers()) && c.isAnnotationPresent(GraphQLTypeExtension.class), //
				c -> new GraphObjectTypeExtension.Data() {

					GraphQLTypeExtension annotation = getSuperclassAnnotationOrThrow(GraphQLTypeExtension.class, c);

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

	@Override
	public GraphObjectTypeExtension createType(Class<?> container) {

		GraphObjectTypeExtension type = new GraphObjectTypeExtension(registry, container);
		dataByReference.computeIfAbsent(type.getReferenceContainer(), k -> new ArrayList<GraphObjectTypeExtension>()).add(type);

		return type;

	}

	@Override
	public GraphQLTypeReference getGraphQLType(Class<?> container) {
		throw new UnsupportedOperationException();
	}

	// methods on reference container

	public List<GraphObjectTypeExtension> getForType(Class<?> referenceContainer) {
		return dataByReference.getOrDefault(referenceContainer, Collections.emptyList());
	}

}
