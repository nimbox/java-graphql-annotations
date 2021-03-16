package com.nimbox.graphql.test.programmatic;

import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaPrinter;

public class BaseStarsWars {

	public static void main(String[] args) {

		GraphQLSchema schema = BaseStarWarsSchema.starWarsSchema;

		SchemaPrinter printer = new SchemaPrinter(
                SchemaPrinter.Options.defaultOptions()
                        .includeScalarTypes(true)
                        // .includeExtendedScalarTypes(true)
                        .includeIntrospectionTypes(false)
                        .includeSchemaDefinition(false)
          );
		
		System.out.println(printer.print(schema));
		
	}

}
