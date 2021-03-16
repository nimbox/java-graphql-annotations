package com.nimbox.graphql.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nimbox.graphql.GraphSchemaBuilder;
import com.nimbox.graphql.scalars.InstantScalar;
import com.nimbox.graphql.scalars.LocalDateTimeScalar;
import com.nimbox.graphql.test.domain.CharacterExtension;
import com.nimbox.graphql.test.domain.Episode;
import com.nimbox.graphql.test.domain.HumanOperations;
import com.nimbox.graphql.test.utils.GraphUtils;
import com.nimbox.util.Alternative;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;

class DomainSchemaTest {

	static final String ARGUMENT = "argument";

	GraphSchemaBuilder builder;

	@BeforeEach
	void beforeEach() {
		builder = new GraphSchemaBuilder();
		builder.withOptional(Alternative.class, Alternative::undefined, Alternative::ofNullable);
		builder.withScalars(InstantScalar.class, LocalDateTimeScalar.class);
		builder.withTypeExtensions(CharacterExtension.class);
		builder.withEnums(Episode.class);
	}

	@Test
	void when_then() {

		builder.withOperations(HumanOperations.class);

		GraphQLSchema schema = builder.build();
		GraphUtils.printSchema(schema);
		GraphQL graphQL = GraphQL.newGraphQL(schema).build();

//		ExecutionResult result = graphQL.execute("{ getHumans(limit: 123, age: 22) { id name } }");
//		ExecutionResult result = graphQL.execute("{ getHumanInput(input: { name: \"Ricardo\" } ) { id name } }");

		ExecutionResult result = graphQL.execute("{ getHumansByEpisode(episode: NEWHOPE, age: 81) { getFriends { id name } } }");
//		ExecutionResult result = graphQL.execute("{ getHumansByEpisode(episode: NEWHOPE) { getFriends { id name } } }");

		System.out.println(result);

	}

}
