package com.nimbox.graphql.runtime;

import java.util.Map;

public interface ArgumentExtractor {

	public abstract Object apply(Map<String, Object> arguments, RuntimeParameter parameter) throws Exception;

}