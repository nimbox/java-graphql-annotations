package com.nimbox.graphql.runtime;

import graphql.schema.DataFetchingEnvironment;

public interface RuntimeParameterExtractor {

	public abstract Object apply(DataFetchingEnvironment environment, RuntimeParameter parameter) throws Exception;

}
