package com.nimbox.graphql.runtime;

import com.nimbox.graphql.types.GraphValueClass;

import graphql.schema.DataFetchingEnvironment;

public interface RuntimeParameterFetcher {

	public <T> T get(final DataFetchingEnvironment environment, final GraphValueClass valueClass, final String name);

}
