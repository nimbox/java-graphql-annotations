package com.nimbox.graphql;

import static graphql.schema.GraphqlTypeComparatorRegistry.AS_IS_REGISTRY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.runtime.RuntimeParameterFactory;
import com.nimbox.graphql.types.GraphEnumType;
import com.nimbox.graphql.types.GraphEnumTypeValue;
import com.nimbox.graphql.types.GraphInputObjectType;
import com.nimbox.graphql.types.GraphMutation;
import com.nimbox.graphql.types.GraphObjectType;
import com.nimbox.graphql.types.GraphObjectTypeExtension;
import com.nimbox.graphql.types.GraphObjectTypeExtensionField;
import com.nimbox.graphql.types.GraphObjectTypeField;
import com.nimbox.graphql.types.GraphOptionalDefinition;
import com.nimbox.graphql.types.GraphQueryField;
import com.nimbox.graphql.types.GraphScalarType;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLCodeRegistry;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;

public class GraphSchemaBuilder {

//	private static final Logger _logger = LoggerFactory.getLogger(GraphSchemaBuilder.class);

	// properties

	private List<GeneratorPackage> packages = new ArrayList<GeneratorPackage>();
	private List<GraphOptionalDefinition<?>> optionals = new ArrayList<GraphOptionalDefinition<?>>();

	private List<Class<?>> scalars = new ArrayList<Class<?>>();
	private List<Class<?>> enums = new ArrayList<Class<?>>();

	private List<Class<?>> types = new ArrayList<Class<?>>();
	private List<Class<?>> typeExtensions = new ArrayList<Class<?>>();
	private List<Class<?>> operations = new ArrayList<Class<?>>();

	// constructors

	public GraphSchemaBuilder() {
	}

	// configurations

	public GraphSchemaBuilder withPackages(ClassLoader classLoader, String... packages) {
		this.packages.add(new GeneratorPackage(classLoader, packages));
		return this;
	}

	public <U> GraphSchemaBuilder withOptional(Class<U> klass, Supplier<U> undefined, Function<Object, U> nullable) {
		this.optionals.add(new GraphOptionalDefinition<>(klass, undefined, nullable));
		return this;
	}

	public GraphSchemaBuilder withOperations(Class<?>... rootTypeClasses) {
		this.operations.addAll(Arrays.asList(rootTypeClasses));
		return this;
	}

	public GraphSchemaBuilder withScalars(Class<?>... scalars) {
		this.scalars.addAll(Arrays.asList(scalars));
		return this;
	}

	public GraphSchemaBuilder withEnums(Class<?>... enums) {
		this.enums.addAll(Arrays.asList(enums));
		return this;
	}

	public GraphSchemaBuilder withTypes(Class<?>... types) {
		this.typeExtensions.addAll(Arrays.asList(types));
		return this;
	}

	public GraphSchemaBuilder withTypeExtensions(Class<?>... typeExtensions) {
		this.typeExtensions.addAll(Arrays.asList(typeExtensions));
		return this;
	}

	// generation

	public GraphQLSchema build() {

		// populate the registry

		GraphRegistry registry = new GraphRegistry();
		optionals.forEach(registry::withOptional);

		scalars.stream().forEach(registry.getScalars()::of);
		enums.stream().forEach(registry.getEnums()::of);

		operations.stream().forEach(registry.getQueries()::of);
		operations.stream().forEach(registry.getMutations()::of);

		types.stream().forEach(registry.getObjects()::of);
		typeExtensions.stream().forEach(registry.getObjectExtensions()::of);

		//

		GraphQLSchema.Builder builder = GraphQLSchema.newSchema();
		GraphQLCodeRegistry.Builder codeBuilder = GraphQLCodeRegistry.newCodeRegistry();

		RuntimeParameterFactory factory = new RuntimeParameterFactory();

		//

		buildScalars(registry, builder, codeBuilder, factory);
		buildEnums(registry, builder, codeBuilder, factory);

		// queries

		buildQueries(registry, factory, builder, codeBuilder);

		// mutations

		List<GraphMutation> mutations = registry.getMutations().all();
		if (!mutations.isEmpty()) {

			GraphQLObjectType.Builder mutationBuilder = GraphQLObjectType.newObject();
			mutationBuilder.name("Mutation");
			for (GraphMutation mutation : mutations) {
				mutationBuilder.field(mutation.newFieldDefinition(registry).build());
			}

			builder.mutation(mutationBuilder.build());

		}

		buildObjectTypes(registry, factory, builder, codeBuilder);

		// input objects

		for (GraphInputObjectType inputObject : registry.getInputObjects().all()) {
			builder.additionalType(inputObject.newObjectType(registry).build());
			factory.with(inputObject);
		}

		// code

		// return

		builder.codeRegistry(codeBuilder.build());

		GraphQLSchema schemaDefinition = builder.build();

//		schemaDefinition.getCodeRegistry().getDataFetcher(FieldCoordinates.coordinates("Query", "getHuman"), "getHuman");

		System.out.println("-----");
		System.out.println(factory.toString());
		System.out.println("-----");

		return schemaDefinition;

	}

	//

	private void buildScalars(GraphRegistry registry, GraphQLSchema.Builder builder, GraphQLCodeRegistry.Builder codeBuilder, RuntimeParameterFactory factory) {

		for (GraphScalarType t : registry.getScalars().all()) {

			GraphQLScalarType.Builder scalarTypeBuilder = t.newScalarType(registry);
			builder.additionalType(scalarTypeBuilder.build());

			factory.with(t);

		}

	}

	private void buildEnums(GraphRegistry registry, GraphQLSchema.Builder builder, GraphQLCodeRegistry.Builder codeBuilder, RuntimeParameterFactory factory) {

		for (GraphEnumType t : registry.getEnums().all()) {

			GraphQLEnumType.Builder enumTypeBuilder = t.newEnumType(registry);

			for (GraphEnumTypeValue v : t.getValues().values()) {
				enumTypeBuilder.value(v.newValueDefinition(registry).build());
			}

			builder.additionalType(enumTypeBuilder.build());

			factory.with(t);

		}

	}

	private void buildQueries(GraphRegistry registry, RuntimeParameterFactory factory, GraphQLSchema.Builder builder, GraphQLCodeRegistry.Builder codeBuilder) {

		List<GraphQueryField> queries = registry.getQueries().all();
		if (!queries.isEmpty()) {

			GraphQLObjectType.Builder objectTypeBuilder = GraphQLObjectType.newObject();
			objectTypeBuilder.name("Query");

			List<FieldFetcher> fetchers = new ArrayList<FieldFetcher>();

			for (GraphQueryField query : queries) {
				GraphQLFieldDefinition fieldDefinition = query.newFieldDefinition(registry).build();
				objectTypeBuilder.field(fieldDefinition);
				fetchers.add(new FieldFetcher(fieldDefinition, query.getFetcher(factory)));
			}

			GraphQLObjectType objectTypeDefinition = objectTypeBuilder.build();
			builder.query(objectTypeBuilder.build());

			for (FieldFetcher fetcher : fetchers) {
				codeBuilder.dataFetcher(objectTypeDefinition, fetcher.field, fetcher.dataFetcher);
			}

		}

	}

	private void buildMutations(GraphRegistry registry, RuntimeParameterFactory factory, GraphQLSchema.Builder builder, GraphQLCodeRegistry.Builder codeBuilder) {

		List<GraphQueryField> queries = registry.getQueries().all();
		if (!queries.isEmpty()) {

			GraphQLObjectType.Builder objectTypeBuilder = GraphQLObjectType.newObject();
			objectTypeBuilder.name("Mutation");

			List<FieldFetcher> fetchers = new ArrayList<FieldFetcher>();

			for (GraphQueryField query : queries) {
				GraphQLFieldDefinition fieldDefinition = query.newFieldDefinition(registry).build();
				objectTypeBuilder.field(fieldDefinition);
				fetchers.add(new FieldFetcher(fieldDefinition, query.getFetcher(factory)));
			}

			GraphQLObjectType objectTypeDefinition = objectTypeBuilder.build();
			builder.query(objectTypeBuilder.build());

			for (FieldFetcher fetcher : fetchers) {
				codeBuilder.dataFetcher(objectTypeDefinition, fetcher.field, fetcher.dataFetcher);
			}

		}

	}

	private void buildObjectTypes(GraphRegistry registry, RuntimeParameterFactory factory, GraphQLSchema.Builder builder, GraphQLCodeRegistry.Builder codeBuilder) {

		for (GraphObjectType objectType : registry.getObjects().all()) {

			GraphQLObjectType.Builder objectTypeBuilder = objectType.newObjectType(registry);
			List<FieldFetcher> fetchers = new ArrayList<FieldFetcher>();

			// sort

			Map<String, GraphObjectTypeField> fieldsByName = objectType.getFields().values().stream().collect(Collectors.toMap(GraphObjectTypeField::getName, Function.identity()));
			List<GraphObjectTypeField> fields = objectType.getFieldOrder().stream().map(fieldsByName::remove).filter(Objects::nonNull).collect(Collectors.toList());
			fields.addAll(fieldsByName.values().stream().sorted(Comparator.comparing(GraphObjectTypeField::getName)).collect(Collectors.toList()));
			objectTypeBuilder.comparatorRegistry(AS_IS_REGISTRY);

			// build

			for (GraphObjectTypeField field : fields) {
				GraphQLFieldDefinition fieldDefinition = field.newFieldDefinition(registry).build();
				objectTypeBuilder.field(fieldDefinition);
				fetchers.add(new FieldFetcher(fieldDefinition, field.getFetcher(factory)));
			}

			// extend

			for (GraphObjectTypeExtension extension : registry.getObjectExtensions().getForObjectType(objectType.getObjectTypeClass())) {
				for (GraphObjectTypeExtensionField field : extension.getFields().values()) {
					GraphQLFieldDefinition fieldDefinition = field.newFieldDefinition(registry).build();
					objectTypeBuilder.field(fieldDefinition);
					fetchers.add(new FieldFetcher(fieldDefinition, field.getFetcher(factory)));
				}
			}

			// register

			GraphQLObjectType objectTypeDefinition = objectTypeBuilder.build();
			builder.additionalType(objectTypeBuilder.build());

			// build code

			for (FieldFetcher fetcher : fetchers) {
				codeBuilder.dataFetcher(objectTypeDefinition, fetcher.field, fetcher.dataFetcher);
			}

		}

	}

	//
	// classes
	//

	public static class GeneratorPackage {

		private final ClassLoader classLoader;
		private final String[] packages;

		public GeneratorPackage(ClassLoader classLoader, String... packages) {
			this.classLoader = classLoader;
			this.packages = packages;
		}

		public ClassLoader getClassLoader() {
			return classLoader;
		}

		public String[] getPackages() {
			return packages;
		}

	}

	private static class FieldFetcher {

		private final GraphQLFieldDefinition field;
		private final DataFetcher<?> dataFetcher;

		public FieldFetcher(GraphQLFieldDefinition field, DataFetcher<?> dataFetcher) {
			this.field = field;
			this.dataFetcher = dataFetcher;
		}

	}

}
