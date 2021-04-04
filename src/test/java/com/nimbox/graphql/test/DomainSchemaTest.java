package com.nimbox.graphql.test;

import static com.nimbox.graphql.utils.IntrospectionUtils.getAnnotationOrThrow;
import static com.nimbox.graphql.utils.IntrospectionUtils.getSuperclassAnnotationOrThrow;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nimbox.canexer.api.utils.Entity;
import com.nimbox.canexer.api.utils.Property;
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

		ClassScanner scanner = new ClassScanner().packages("com.nimbox").filter(i -> !i.isAbstract() && i.hasAnnotation(Entity.class.getName()));
		System.out.println(scanner.classes());

		builder = new GraphBuilder();

		builder.withContext(DataFetchingEnvironment.class, environment -> environment);

		builder.withIdExtractors(e -> e.isAnnotationPresent(Property.class) && e.getAnnotation(Property.class).id());		
		builder.withIdCoercing(Long.class, id -> id.toString(), s -> Long.valueOf(s));
		
		builder.withOptional(Alternative.class, Alternative::undefined, Alternative::ofNullable);

		builder.withObjectExtractor( //
				c -> !Modifier.isAbstract(c.getModifiers()) && c.isAnnotationPresent(Entity.class), //
				c -> new GraphObjectType.Data() {

					Entity annotation = getSuperclassAnnotationOrThrow(Entity.class, c);

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
						return Arrays.asList(annotation.order());
					}

				});

		builder.withObjectFieldExtractor( //
				(c, m) -> !Modifier.isAbstract(c.getModifiers()) && m.isAnnotationPresent(Property.class) && m.getAnnotation(Property.class).expose(), //
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
				c -> Modifier.isAbstract(c.getModifiers()) && c.isAnnotationPresent(Entity.class), //
				c -> new GraphInterfaceType.Data() {

					Entity annotation = getSuperclassAnnotationOrThrow(Entity.class, c);

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
						return Arrays.asList(annotation.order());
					}

				});

		builder.withInterfaceFieldExtractor( //
				(c, m) -> Modifier.isAbstract(c.getModifiers()) && m.isAnnotationPresent(Property.class) && m.getAnnotation(Property.class).expose(), //
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

		ExecutionResult result = graphQL.execute("{ getCharacter(id: 999) { id name } }");
//		ExecutionResult result = graphQL.execute("{ getHumans(limit: 123, age: 22) { id name } }");
//		ExecutionResult result = graphQL.execute("{ getHumanInput(input: { name: \"Ricardo\", address: \"Naco\" } ) { id name } }");

//		ExecutionResult result = graphQL.execute("{ getHumansByEpisode(episode: NEWHOPE, age: 81) { getFriends { id name } } }");
//		ExecutionResult result = graphQL.execute("{ getHumansByEpisode(episode: NEWHOPE) { getFriends { id name } } }");

//		ExecutionResult result = graphQL.execute("mutation { createHuman(input: { name: \"Ricardo\", address: \"Naco\" }) { id name } }");

		System.out.println(result);

	}

}
