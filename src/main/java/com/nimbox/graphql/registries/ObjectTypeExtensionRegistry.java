package com.nimbox.graphql.registries;

import java.util.ArrayList;

import com.nimbox.graphql.types.GraphObjectTypeExtension;
import com.nimbox.graphql.types.GraphTypeExtension;

public class ObjectTypeExtensionRegistry extends TypeExtensionRegistry<GraphObjectTypeExtension> {

	// constructors

	public ObjectTypeExtensionRegistry(GraphRegistry registry) {
		super(registry);
	}

	// methods

	public boolean isAcceptable(Class<?> container) {

		GraphTypeExtension.Data data = registry.getObjectExtensions().extractTypeData(container);
		return registry.getObjects().acceptType(data.getType());

	}

	@Override
	public GraphObjectTypeExtension createType(Class<?> container) {

		GraphObjectTypeExtension type = new GraphObjectTypeExtension(registry, container);
		dataByReference.computeIfAbsent(type.getReferenceContainer(), k -> new ArrayList<GraphObjectTypeExtension>()).add(type);
		return type;

	}

}
