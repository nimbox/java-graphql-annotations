package com.nimbox.graphql.registries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nimbox.graphql.GraphBuilderException;

import graphql.schema.GraphQLTypeReference;

public abstract class GraphTypeRegistry<T, C, D> {

	// properties

	final GraphRegistry registry;

	final List<ClassExtractor<C, D>> extractors = new ArrayList<ClassExtractor<C, D>>();
	final Map<C, T> data = new HashMap<C, T>();

	// constructors

	GraphTypeRegistry(final GraphRegistry registry) {
		this.registry = registry;
	}

	// configurators

	@SafeVarargs
	public final void withExtractors(ClassExtractor<C, D>... extractors) {
		this.extractors.addAll(Arrays.asList(extractors));
	}

	public final void withExtractors(List<ClassExtractor<C, D>> extractors) {
		this.extractors.addAll(extractors);
	}

	// configuration methods

	public boolean acceptType(C container) {

		for (ClassExtractor<C, D> extractor : extractors) {
			if (extractor.accept(container)) {
				return true;
			}

		}

		return false;

	}

	public D extractTypeData(C container) {

		for (ClassExtractor<C, D> extractor : extractors) {
			if (extractor.accept(container)) {
				return extractor.extract(container);
			}
		}

		throw new GraphBuilderException(String.format("Unable to extract data from container %s", container));

	}

	// abstract methods

	public abstract T createType(final C container);

	public abstract GraphQLTypeReference getGraphQLType(final C container);

	// registry methods

	public boolean contains(final C container) {

		if (data.containsKey(container)) {
			return true;
		}

		if (acceptType(container)) {
			compute(container);
			return true;
		}

		return false;

	}

	public T compute(final C container) {

		if (data.containsKey(container)) {
			return data.get(container);
		}

		T type = createType(container);
		data.put(container, type);

		return type;

	}

	public Collection<T> all() {
		return data.values();

	}

}
