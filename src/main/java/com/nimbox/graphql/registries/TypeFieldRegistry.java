package com.nimbox.graphql.registries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nimbox.graphql.GraphBuilderException;

public abstract class TypeFieldRegistry<T, C, F, CD, FD> extends TypeRegistry<T, C, CD> {

	// properties

	final List<ClassFieldExtractor<C, F, FD>> fieldExtractors = new ArrayList<ClassFieldExtractor<C, F, FD>>();

	// constructors

	TypeFieldRegistry(GraphRegistry registry) {
		super(registry);
	}

	// configurators

	@SafeVarargs
	public final void withFieldExtractors(ClassFieldExtractor<C, F, FD>... fieldExtractors) {
		this.fieldExtractors.addAll(Arrays.asList(fieldExtractors));
	}

	public final void withFieldExtractors(List<ClassFieldExtractor<C, F, FD>> fieldExtractors) {
		this.fieldExtractors.addAll(fieldExtractors);
	}

	// configuration methods

	public boolean acceptTypeField(C container, F field) {

		for (ClassFieldExtractor<C, F, FD> extractor : fieldExtractors) {
			if (extractor.accept(container, field)) {
				return true;
			}

		}

		return false;

	}

	public FD extractTypeFieldData(C container, F field) {

		for (ClassFieldExtractor<C, F, FD> extractor : fieldExtractors) {
			if (extractor.accept(container, field)) {
				return extractor.extract(container, field);
			}
		}

		throw new GraphBuilderException(String.format("Unable to extract field data from field %s in container %s", field, container));

	}

}
