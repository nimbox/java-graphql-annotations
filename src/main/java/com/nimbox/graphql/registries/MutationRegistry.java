package com.nimbox.graphql.registries;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.nimbox.graphql.annotations.GraphQLMutation;
import com.nimbox.graphql.types.GraphMutation;

public class MutationRegistry {

	// properties

	private final GraphRegistry registry;
	private Map<Class<?>, Map<Method, GraphMutation>> data = new HashMap<Class<?>, Map<Method, GraphMutation>>();

	// constructors

	public MutationRegistry(GraphRegistry registry) {
		this.registry = registry;
	}

	public Map<Method, GraphMutation> of(final Class<?> typeClass) {

		if (data.containsKey(typeClass)) {
			return data.get(typeClass);
		}

		// create

		Map<Method, GraphMutation> queries = new HashMap<Method, GraphMutation>();
		data.put(typeClass, queries);

		for (Method method : typeClass.getMethods()) {
			if (method.isAnnotationPresent(GraphQLMutation.class)) {
				queries.put(method, new GraphMutation(registry, method));
			}
		}

		// return

		return queries;

	}

	// getters

	public List<GraphMutation> all() {
		return data.values().stream().flatMap(m -> m.values().stream()).collect(Collectors.toList());
	}

}
