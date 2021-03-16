package com.nimbox.graphql.parameters.arguments;

import java.util.Map;

import com.nimbox.graphql.types.GraphValueClass;

public abstract class Argument {

	public abstract Object instance(ArgumentFactory factory, final GraphValueClass returnType, final Map<String, Object> arguments, final String name) throws Exception;

}