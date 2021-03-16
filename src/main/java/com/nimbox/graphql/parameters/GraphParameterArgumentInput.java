package com.nimbox.graphql.parameters;

import java.lang.reflect.Parameter;

import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.types.GraphValueClass;

public class GraphParameterArgumentInput extends GraphParameterArgument {

	public GraphParameterArgumentInput(final GraphRegistry registry, final Parameter fieldParameter, final GraphValueClass valueType) {
		super(registry, fieldParameter, valueType);
	}

}
