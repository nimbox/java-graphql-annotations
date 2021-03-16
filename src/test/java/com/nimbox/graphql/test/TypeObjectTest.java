package com.nimbox.graphql.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nimbox.graphql.GraphSchemaBuilder;
import com.nimbox.graphql.annotations.GraphQLArgument;
import com.nimbox.graphql.annotations.GraphQLQuery;
import com.nimbox.graphql.annotations.GraphQLType;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.test.domain.HumanOperations;
import com.nimbox.graphql.test.utils.GraphUtils;
import com.nimbox.graphql.types.GraphOptionalDefinition;
import com.nimbox.util.Alternative;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;

class TypeObjectTest {

	static final String ARGUMENT = "argument";

	GraphRegistry registry;

	@BeforeEach
	void beforeEach() {
		registry = new GraphRegistry(ExecutionContext.class);
		registry.withOptional(new GraphOptionalDefinition<>(Alternative.class, Alternative::undefined, Alternative::ofNullable));
	}

	@Test
	void when_then() {

		Object object = new @GraphQLType(name = "SomeThing", description = "SomeThing description.") Object() {

			@GraphQLQuery(name = "method", description = "Method description.")
			public String method(@GraphQLArgument(name = ARGUMENT) String argument) {
				return "s";
			}

			@GraphQLQuery(name = "ass", description = "Method description.")
			public String method2(@GraphQLArgument(name = ARGUMENT) String argument) {
				return "s";
			}

		};

		GraphSchemaBuilder builder = new GraphSchemaBuilder();
		builder.withOptional(Alternative.class, Alternative::undefined, Alternative::ofNullable);
		builder.withOperations(HumanOperations.class);

		GraphQLSchema schema = builder.build();
		GraphUtils.printSchema(schema);
		GraphQL graphQL = GraphQL.newGraphQL(schema).build();

//		ExecutionResult result = graphQL.execute("");

//		GraphObjectType objectType = new GraphObjectType(registry, object.getClass());

//		registry.getTypes().of(object.getClass());
//		System.out.println(registry.getTypes());
//
//		GraphObjectType type = registry.getTypes().get(object.getClass());
//
//		System.out.println(type.getName());
//		System.out.println(type.getDescription());
//		System.out.println(type.getQueries());
//
//		GraphQLObjectType ot = type.newObjectType(registry).build();
//		System.out.println(ot);
//		ot.getFieldDefinitions().stream().forEach(out::println);

	}

	//
	//
	//

	public static class ExecutionContext {

		public String getName() {
			return "Some name";
		}

	}

}
