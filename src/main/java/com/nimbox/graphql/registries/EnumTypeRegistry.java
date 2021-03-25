package com.nimbox.graphql.registries;

import static com.nimbox.graphql.utils.IntrospectionUtils.getTypeAnnotationOrThrow;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.nimbox.graphql.GraphBuilderException;
import com.nimbox.graphql.annotations.GraphQLEnum;
import com.nimbox.graphql.types.GraphEnumType;

public class EnumTypeRegistry {

	// properties

	private final GraphRegistry registry;

	private Map<Class<?>, GraphEnumType> data = new HashMap<Class<?>, GraphEnumType>();
	private Map<String, Class<?>> names = new HashMap<String, Class<?>>();

	// constructors

	public EnumTypeRegistry(GraphRegistry registry) {
		this.registry = registry;
	}

	public GraphEnumType of(final Class<?> enumTypeClass) {

		if (data.containsKey(enumTypeClass)) {
			return data.get(enumTypeClass);
		}

		// check the name is not duplicated

		GraphQLEnum annotation = getTypeAnnotationOrThrow(GraphQLEnum.class, enumTypeClass);
		if (names.containsKey(annotation.name())) {
			throw new GraphBuilderException(String.format("Type %s has same name as type %s", enumTypeClass, names.get(annotation.name())));
		}

		// create

		GraphEnumType enumType = new GraphEnumType(registry, enumTypeClass);
		data.put(enumTypeClass, enumType);
		names.put(enumType.getName(), enumTypeClass);

		// return

		return enumType;

	}

	// getters

	public Collection<GraphEnumType> all() {
		return data.values();
	}

	public GraphEnumType get(Class<?> enumTypeClass) {
		return data.get(enumTypeClass);
	}

}
