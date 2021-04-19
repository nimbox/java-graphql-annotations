package com.nimbox.graphql.registries;

import java.util.ArrayList;

import com.nimbox.graphql.types.GraphInterfaceTypeExtension;
import com.nimbox.graphql.types.GraphTypeExtension;

public class InterfaceTypeExtensionRegistry extends TypeExtensionRegistry<GraphInterfaceTypeExtension> {

	// constructors

	public InterfaceTypeExtensionRegistry(GraphRegistry registry) {
		super(registry);
	}

	// methods

	@Override
	public boolean isAcceptable(Class<?> container) {

		GraphTypeExtension.Data data = registry.getInterfaceExtensions().extractTypeData(container);
		return registry.getInterfaces().acceptType(data.getType());

	}

	@Override
	public GraphInterfaceTypeExtension createType(Class<?> container) {

		GraphInterfaceTypeExtension type = new GraphInterfaceTypeExtension(registry, container);
		dataByReference.computeIfAbsent(type.getReferenceContainer(), k -> new ArrayList<GraphInterfaceTypeExtension>()).add(type);
		return type;

	}

}
