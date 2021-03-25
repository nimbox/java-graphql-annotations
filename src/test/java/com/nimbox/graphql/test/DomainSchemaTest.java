package com.nimbox.graphql.test;

import java.time.Instant;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nimbox.canexer.locals.api.utils.Kind;
import com.nimbox.canexer.locals.api.utils.Property;
import com.nimbox.graphql.GraphBuilder;
import com.nimbox.graphql.scalars.InstantScalar;
import com.nimbox.graphql.scalars.LocalDateTimeScalar;
import com.nimbox.graphql.scanners.Scanner;
import com.nimbox.graphql.test.domain.CharacterExtension;
import com.nimbox.graphql.test.domain.Episode;
import com.nimbox.graphql.test.domain.HumanOperations;
import com.nimbox.graphql.utils.GraphUtils;
import com.nimbox.util.Alternative;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;

class DomainSchemaTest {

	static final String ARGUMENT = "argument";

	GraphBuilder builder;

	@BeforeEach
	void beforeEach() {

		Scanner scanner = new Scanner().packages("com.nimbox").filter(i -> i.hasAnnotation(Kind.class.getName()));
		System.out.println(scanner.classes());

		builder = new GraphBuilder();

		builder.withObjectAnnotation(Kind.class, Kind::name, Kind::description, Kind::fieldOrder);
		builder.withObjectFieldAnnotation(Property.class, Property::value, null, null);

		builder.withOptional(Alternative.class, Alternative::undefined, Alternative::ofNullable);

		builder.withScalar(Instant.class, InstantScalar.class);
		builder.withScalar(LocalDateTime.class, LocalDateTimeScalar.class);

		builder.withTypeExtensions(CharacterExtension.class);
		builder.withEnums(Episode.class);

		builder.withObjects(scanner.classes());

	}

	@Test
	void when_then() {

		builder.withOperations(HumanOperations.class);

		GraphQLSchema schema = builder.build();
		GraphUtils.printSchema(schema);
		GraphQL graphQL = GraphQL.newGraphQL(schema).build();

//		ExecutionResult result = graphQL.execute("{ getCharacter { id name } }");
//		ExecutionResult result = graphQL.execute("{ getHumans(limit: 123, age: 22) { id name } }");
//		ExecutionResult result = graphQL.execute("{ getHumanInput(input: { name: \"Ricardo\", address: \"Naco\" } ) { id name } }");

//		ExecutionResult result = graphQL.execute("{ getHumansByEpisode(episode: NEWHOPE, age: 81) { getFriends { id name } } }");
//		ExecutionResult result = graphQL.execute("{ getHumansByEpisode(episode: NEWHOPE) { getFriends { id name } } }");

		ExecutionResult result = graphQL.execute("mutation { createHuman(input: { name: \"Ricardo\", address: \"Naco\" }) { id name } }");

		System.out.println(result);

	}

}
