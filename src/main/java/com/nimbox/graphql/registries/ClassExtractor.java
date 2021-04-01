package com.nimbox.graphql.registries;

import java.util.function.Function;
import java.util.function.Predicate;

public class ClassExtractor<C, D> {

	// properties

	private final Predicate<C> accept;
	private final Function<C, D> extract;

	// constructors

	public ClassExtractor(Predicate<C> accept, Function<C, D> extract) {
		this.accept = accept;
		this.extract = extract;
	}

	// method

	public boolean accept(C container) {
		return accept.test(container);
	}

	public D extract(C container) {
		return extract.apply(container);
	}

}
