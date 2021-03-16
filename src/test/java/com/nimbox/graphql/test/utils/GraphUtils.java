package com.nimbox.graphql.test.utils;

import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaPrinter;

import static graphql.schema.GraphqlTypeComparatorRegistry.AS_IS_REGISTRY;

public class GraphUtils {

	public static void printSchema(GraphQLSchema schema) {

		SchemaPrinter printer = new SchemaPrinter( //
				SchemaPrinter.Options.defaultOptions() //
						.setComparators(AS_IS_REGISTRY) //
						.includeScalarTypes(true) //
						.includeIntrospectionTypes(false) //
						.includeSchemaDefinition(false) //
		);

		System.out.println(printer.print(schema));

	}

}
