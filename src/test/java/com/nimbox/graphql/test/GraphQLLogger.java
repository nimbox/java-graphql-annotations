package com.nimbox.graphql.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphQLLogger {

	private static final Logger _logger = LoggerFactory.getLogger(GraphQLLogger.class);

//	private static final Logger _logger4j = LogManager.getLogger(GraphQLLogger.class);

	public static void main(String[] args) {
		_logger.info("hola");
	}

}
