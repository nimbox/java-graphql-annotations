package com.nimbox.graphql.registries;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class ClassFieldExtractor<C, F, FD> {

	// properties

	private final BiPredicate<C, F> accept;
	private final BiFunction<C, F, FD> extractor;

	// constructors

	public ClassFieldExtractor(BiPredicate<C, F> accept, BiFunction<C, F, FD> extract) {
		this.accept = accept;
		this.extractor = extract;
	}

	// method

	public boolean accept(C container, F field) {
		return accept.test(container, field);
	}

	public FD extract(C container, F field) {
		return extractor.apply(container, field);
	}

}
