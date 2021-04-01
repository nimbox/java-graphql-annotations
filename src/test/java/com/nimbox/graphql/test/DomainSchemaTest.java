package com.nimbox.graphql.test;

import static com.nimbox.graphql.utils.IntrospectionUtils.getAnnotationOrThrow;
import static com.nimbox.graphql.utils.IntrospectionUtils.getSuperclassAnnotationOrThrow;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nimbox.canexer.locals.api.utils.Kind;
import com.nimbox.canexer.locals.api.utils.Property;
import com.nimbox.graphql.GraphBuilder;
import com.nimbox.graphql.scalars.InstantScalar;
import com.nimbox.graphql.scalars.LocalDateTimeScalar;
import com.nimbox.graphql.scanners.ClassScanner;
import com.nimbox.graphql.test.domain.CharacterExtension;
import com.nimbox.graphql.test.domain.Episode;
import com.nimbox.graphql.test.domain.HumanOperations;
import com.nimbox.graphql.types.GraphInterfaceType;
import com.nimbox.graphql.types.GraphInterfaceTypeField;
import com.nimbox.graphql.types.GraphObjectType;
import com.nimbox.graphql.types.GraphObjectTypeField;
import com.nimbox.graphql.utils.GraphUtils;
import com.nimbox.util.Alternative;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;

class DomainSchemaTest {

	static final String ARGUMENT = "argument";

	GraphBuilder builder;

	@BeforeEach
	void beforeEach() {

		ClassScanner scanner = new ClassScanner().packages("com.nimbox").filter(i -> !i.isAbstract() && i.hasAnnotation(Kind.class.getName()));
		System.out.println(scanner.classes());

		builder = new GraphBuilder();

		builder.withOptional(Alternative.class, Alternative::undefined, Alternative::ofNullable);
		builder.withContext(DataFetchingEnvironment.class, environment -> environment);

		builder.withObjectExtractor( //
				c -> !Modifier.isAbstract(c.getModifiers()) && c.isAnnotationPresent(Kind.class), //
				c -> new GraphObjectType.Data() {

					Kind annotation = getSuperclassAnnotationOrThrow(Kind.class, c);

					@Override
					public String getName() {
						return annotation.name();
					}

					@Override
					public String getDescription() {
						return annotation.description();
					}

					@Override
					public List<String> getOrder() {
						return Collections.emptyList();
					}

				});

		builder.withObjectFieldExtractor( //
				(c, m) -> !Modifier.isAbstract(c.getModifiers()) && m.isAnnotationPresent(Property.class), //
				(c, m) -> new GraphObjectTypeField.Data() {

					Property annotation = getAnnotationOrThrow(Property.class, m);

					@Override
					public String getName() {
						return annotation.value();
					}

					@Override
					public String getDescription() {
						return null;
					}

					@Override
					public String getDeprecationReason() {
						return null;
					}

				});

		builder.withInterfaceExtractor( //
				c -> Modifier.isAbstract(c.getModifiers()) && c.isAnnotationPresent(Kind.class), //
				c -> new GraphInterfaceType.Data() {

					Kind annotation = getSuperclassAnnotationOrThrow(Kind.class, c);

					@Override
					public String getName() {
						return annotation.name();
					}

					@Override
					public String getDescription() {
						return annotation.description();
					}

					@Override
					public List<String> getOrder() {
						return Collections.emptyList();
					}

				});

		builder.withInterfaceFieldExtractor( //
				(c, m) -> Modifier.isAbstract(c.getModifiers()) && m.isAnnotationPresent(Property.class), //
				(c, m) -> new GraphInterfaceTypeField.Data() {

					Property annotation = getAnnotationOrThrow(Property.class, m);

					@Override
					public String getName() {
						return annotation.value();
					}

					@Override
					public String getDescription() {
						return null;
					}

					@Override
					public String getDeprecationReason() {
						return null;
					}

				});

		builder.withScalars(InstantScalar.class);
		builder.withScalars(LocalDateTimeScalar.class);

		builder.withObjectExtensions(CharacterExtension.class);
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
