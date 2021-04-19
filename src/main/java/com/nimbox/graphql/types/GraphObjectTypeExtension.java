package com.nimbox.graphql.types;

import com.nimbox.graphql.registries.GraphRegistry;

public class GraphObjectTypeExtension extends GraphTypeExtension {

	// constructors

	public GraphObjectTypeExtension(final GraphRegistry registry, final Class<?> container) {
		super(registry, container, registry.getObjectExtensions().extractTypeData(container));
	}

}
