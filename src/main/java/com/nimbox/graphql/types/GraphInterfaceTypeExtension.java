package com.nimbox.graphql.types;

import com.nimbox.graphql.registries.GraphRegistry;

public class GraphInterfaceTypeExtension extends GraphTypeExtension {

	// constructors

	public GraphInterfaceTypeExtension(final GraphRegistry registry, final Class<?> container) {
		super(registry, container, registry.getInterfaceExtensions().extractTypeData(container));
	}

}
