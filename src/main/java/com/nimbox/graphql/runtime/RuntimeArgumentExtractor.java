package com.nimbox.graphql.runtime;

import java.util.Map;

public interface RuntimeArgumentExtractor {

	public abstract Object apply(Map<String, Object> arguments, RuntimeParameter parameter) throws Exception;

}