package com.nimbox.graphql.runtime;

import java.util.Map;

public interface RuntimeIdExtractor {

	public abstract Object apply(Map<String, Object> arguments, RuntimeParameter parameter) throws Exception;

}