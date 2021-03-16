package com.nimbox.graphql.parameters;

import java.lang.reflect.Parameter;

import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.types.GraphValueClass;

public class GraphParameterArgumentEnum extends GraphParameterArgument {

	public GraphParameterArgumentEnum(final GraphRegistry registry, final Parameter parameter, final GraphValueClass valueType) {
		super(registry, parameter, valueType);
	}

}
