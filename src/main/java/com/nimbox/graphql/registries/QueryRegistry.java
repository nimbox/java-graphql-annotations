package com.nimbox.graphql.registries;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.nimbox.graphql.annotations.GraphQLQuery;
import com.nimbox.graphql.types.GraphQueryField;

public class QueryRegistry {

	// properties

	private final GraphRegistry registry;
	private Map<Class<?>, Map<Method, GraphQueryField>> data = new HashMap<Class<?>, Map<Method, GraphQueryField>>();

	// constructors

	public QueryRegistry(GraphRegistry registry) {
		this.registry = registry;
	}

	public Map<Method, GraphQueryField> of(final Class<?> typeClass) {

		if (data.containsKey(typeClass)) {
			return data.get(typeClass);
		}

		// create

		Map<Method, GraphQueryField> queries = new HashMap<Method, GraphQueryField>();
		data.put(typeClass, queries);

		for (Method fieldMethod : typeClass.getMethods()) {
			if (fieldMethod.isAnnotationPresent(GraphQLQuery.class)) {
				queries.put(fieldMethod, new GraphQueryField(registry, typeClass, fieldMethod));
			}
		}

		// return

		return queries;

	}

	// getters

	public List<GraphQueryField> all() {
		return data.values().stream().flatMap(m -> m.values().stream()).collect(Collectors.toList());
	}

}
