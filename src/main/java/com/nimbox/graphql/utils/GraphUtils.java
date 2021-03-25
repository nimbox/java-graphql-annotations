package com.nimbox.graphql.utils;

import static graphql.schema.GraphqlTypeComparatorRegistry.AS_IS_REGISTRY;

import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaPrinter;

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
