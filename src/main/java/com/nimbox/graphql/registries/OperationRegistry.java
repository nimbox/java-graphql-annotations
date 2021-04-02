package com.nimbox.graphql.registries;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.nimbox.graphql.GraphBuilderException;
import com.nimbox.graphql.types.GraphField;

abstract class OperationRegistry<T> {

	// properties

	final GraphRegistry registry;

	final List<ClassFieldExtractor<Class<?>, Method, GraphField.Data>> fieldExtractors = new ArrayList<>();
	final Map<Class<?>, Map<Method, T>> data = new HashMap<>();

	// constructors

	OperationRegistry(final GraphRegistry registry) {
		this.registry = registry;
	}

	// configurators

	@SafeVarargs
	public final void withFieldExtractors(final ClassFieldExtractor<Class<?>, Method, GraphField.Data>... fieldExtractors) {
		this.fieldExtractors.addAll(Arrays.asList(fieldExtractors));
	}

	public final void withFieldExtractors(final List<ClassFieldExtractor<Class<?>, Method, GraphField.Data>> fieldExtractors) {
		this.fieldExtractors.addAll(fieldExtractors);
	}

	// configuration methods

	public boolean acceptTypeField(Class<?> container, Method field) {

		for (ClassFieldExtractor<Class<?>, Method, GraphField.Data> extractor : fieldExtractors) {
			if (extractor.accept(container, field)) {
				return true;
			}

		}

		return false;

	}

	public GraphField.Data extractTypeFieldData(Class<?> container, Method field) {

		for (ClassFieldExtractor<Class<?>, Method, GraphField.Data> extractor : fieldExtractors) {
			if (extractor.accept(container, field)) {
				return extractor.extract(container, field);
			}
		}

		throw new GraphBuilderException(String.format("Unable to extract field data from field %s in container %s", field, container));

	}

	// abstract methods

	public abstract T createType(final Class<?> container, final Method method);

	// registry methods

	public Map<Method, T> compute(final Class<?> container) {

		if (data.containsKey(container)) {
			return data.get(container);
		}

		// create

		Map<Method, T> queries = new HashMap<>();
		data.put(container, queries);

		for (Method method : container.getMethods()) {
			if (acceptTypeField(container, method)) {
				queries.put(method, createType(container, method));
			}
		}

		// return

		return queries;

	}

	public Collection<T> all() {
		return data.values().stream().flatMap(m -> m.values().stream()).collect(Collectors.toList());
	}

}
