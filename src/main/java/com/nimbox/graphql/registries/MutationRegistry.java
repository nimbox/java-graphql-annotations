package com.nimbox.graphql.registries;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.nimbox.graphql.annotations.GraphQLMutation;
import com.nimbox.graphql.types.GraphMutationField;

public class MutationRegistry {

	// properties

	private final GraphRegistry registry;
	private Map<Class<?>, Map<Method, GraphMutationField>> data = new HashMap<Class<?>, Map<Method, GraphMutationField>>();

	// constructors

	public MutationRegistry(GraphRegistry registry) {
		this.registry = registry;
	}

	public Map<Method, GraphMutationField> of(final Class<?> typeClass) {

		if (data.containsKey(typeClass)) {
			return data.get(typeClass);
		}

		// create

		Map<Method, GraphMutationField> queries = new HashMap<Method, GraphMutationField>();
		data.put(typeClass, queries);

		for (Method fieldMethod : typeClass.getMethods()) {
			if (fieldMethod.isAnnotationPresent(GraphQLMutation.class)) {
				queries.put(fieldMethod, new GraphMutationField(registry, typeClass, fieldMethod));
			}
		}

		// return

		return queries;

	}

	// getters

	public List<GraphMutationField> all() {
		return data.values().stream().flatMap(m -> m.values().stream()).collect(Collectors.toList());
	}

}
