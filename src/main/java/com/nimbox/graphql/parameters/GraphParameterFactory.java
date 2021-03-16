package com.nimbox.graphql.parameters;

import java.util.HashMap;
import java.util.Map;

public class GraphParameterFactory {

	// properties

	private Map<Class<?>, ParameterFetcher> fetchers = new HashMap<Class<?>, ParameterFetcher>();

	// constructors

	public GraphParameterFactory() {

	}

	// classes

	public static interface ParameterFetcher {

		public String getName();

		public <T> T getValue();

	}

}
