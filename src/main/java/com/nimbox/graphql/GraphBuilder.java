package com.nimbox.graphql;

import static graphql.schema.GraphQLTypeReference.typeRef;
import static graphql.schema.GraphqlTypeComparatorRegistry.AS_IS_REGISTRY;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.registries.GraphRegistry.FieldAnnotation;
import com.nimbox.graphql.registries.GraphRegistry.TypeAnnotation;
import com.nimbox.graphql.runtime.RuntimeParameterFactory;
import com.nimbox.graphql.types.GraphEnumType;
import com.nimbox.graphql.types.GraphEnumTypeValue;
import com.nimbox.graphql.types.GraphInputObjectType;
import com.nimbox.graphql.types.GraphInterfaceType;
import com.nimbox.graphql.types.GraphMutationField;
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
import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;

public class GraphBuilder {

//	private static final Logger _logger = LoggerFactory.getLogger(GraphSchemaBuilder.class);

	// properties

	private List<GeneratorPackage> packages = new ArrayList<GeneratorPackage>();
	private List<GraphOptionalDefinition<?>> optionals = new ArrayList<GraphOptionalDefinition<?>>();

	private final List<TypeAnnotation<?>> typeAnnotations = new ArrayList<TypeAnnotation<?>>();
	private final List<FieldAnnotation<?>> fieldAnnotations = new ArrayList<FieldAnnotation<?>>();

	private Map<Class<?>, Class<?>> scalars = new HashMap<Class<?>, Class<?>>();
	private List<Class<?>> enums = new ArrayList<Class<?>>();

	private List<Class<?>> objects = new ArrayList<Class<?>>();
	private List<Class<?>> objectExtensions = new ArrayList<Class<?>>();

	private List<Class<?>> operations = new ArrayList<Class<?>>();

	private Map<Class<?>, Map<String, DataFetcher<?>>> contexts = new HashMap<Class<?>, Map<String, DataFetcher<?>>>();

	// constructors

	public GraphBuilder() {
	}

	// configurations

	public GraphBuilder withPackages(ClassLoader classLoader, String... packages) {
		this.packages.add(new GeneratorPackage(classLoader, packages));
		return this;
	}

	public <U> GraphBuilder withOptional(Class<U> klass, Supplier<U> undefined, Function<Object, U> nullable) {
		this.optionals.add(new GraphOptionalDefinition<>(klass, undefined, nullable));
		return this;
	}

	public GraphBuilder withOperations(Class<?>... operations) {
		this.operations.addAll(Arrays.asList(operations));
		return this;
	}

	public GraphBuilder withOperations(List<Class<?>> operations) {
		this.operations.addAll(operations);
		return this;
	}

	public GraphBuilder withScalar(Class<?> javaClass, Class<?> scalarTypeClass) {
		this.scalars.put(javaClass, scalarTypeClass);
		return this;
	}

	public GraphBuilder withEnums(Class<?>... enums) {
		this.enums.addAll(Arrays.asList(enums));
		return this;
	}

	public <T> GraphBuilder withContext(Class<T> context, DataFetcher<T> fetcher) {
		return withContext(context, null, fetcher);
	}

	public <T> GraphBuilder withContext(Class<T> context, String name, DataFetcher<T> fetcher) {
		contexts.computeIfAbsent(context, k -> new HashMap<String, DataFetcher<?>>()).put(name, fetcher);
		return this;
	}

	//

	public <T extends Annotation> GraphBuilder withObjectAnnotation(Class<T> annotationClass, Function<T, String> getName, Function<T, String> getDescription, Function<T, String[]> getFieldOrder) {
		typeAnnotations.add(new TypeAnnotation<T>(annotationClass, getName, getDescription, getFieldOrder));
		return this;
	}

	public GraphBuilder withObjects(Class<?>... types) {
		this.objects.addAll(Arrays.asList(types));
		return this;
	}

	public GraphBuilder withObjects(List<Class<?>> types) {
		this.objects.addAll(types);
		return this;
	}

	//

	public <T extends Annotation> GraphBuilder withObjectFieldAnnotation(Class<T> annotationClass, Function<T, String> getName, Function<T, String> getDescription, Function<T, String> getDeprecationReason) {
		fieldAnnotations.add(new FieldAnnotation<T>(annotationClass, getName, getDescription, getDeprecationReason));
		return this;
	}

	//

	public GraphBuilder withTypeExtensions(Class<?>... typeExtensions) {
		this.objectExtensions.addAll(Arrays.asList(typeExtensions));
		return this;
	}

	// generation

//	@SuppressWarnings("unchecked")
	public GraphQLSchema build() {

		// populate the registry

		GraphRegistry registry = new GraphRegistry();

		// extended annotations

		registry.withTypeAnnotations(typeAnnotations);
		registry.withFieldAnnotations(fieldAnnotations);

		//

		optionals.forEach(registry::withOptional);

		scalars.entrySet().stream().forEach(e -> registry.getScalars().of(e.getKey(), e.getValue()));
		enums.stream().forEach(registry.getEnums()::of);

		operations.stream().forEach(registry.getQueries()::of);
		operations.stream().forEach(registry.getMutations()::of);

		objects.stream().forEach(registry.getObjects()::of);
		objectExtensions.stream().forEach(registry.getObjectExtensions()::of);

		// the registry is full at this point

		GraphQLSchema.Builder builder = GraphQLSchema.newSchema();
		GraphQLCodeRegistry.Builder codeBuilder = GraphQLCodeRegistry.newCodeRegistry();
		RuntimeParameterFactory factory = new RuntimeParameterFactory();

		// contexts

		for (Map.Entry<Class<?>, Map<String, DataFetcher<?>>> e : contexts.entrySet()) {
			for (Map.Entry<String, DataFetcher<?>> f : e.getValue().entrySet()) {
				factory.with((Class<Object>) e.getKey(), f.getKey(), (DataFetcher<Object>) f.getValue());
			}
		}

		// base

		buildScalars(registry, builder, codeBuilder, factory);
		buildEnums(registry, builder, codeBuilder, factory);

		// objects

		buildInterfaceTypes(registry, factory, builder, codeBuilder);
		buildObjectTypes(registry, factory, builder, codeBuilder);

		// operations

		buildQueries(registry, factory, builder, codeBuilder);
		buildMutations(registry, factory, builder, codeBuilder);

		// input objects

		for (GraphInputObjectType inputObject : registry.getInputObjects().all()) {
			builder.additionalType(inputObject.newObjectType(registry).build());
			factory.with(inputObject);
		}

		// code

		builder.codeRegistry(codeBuilder.build());

		// return

		return builder.build();

	}

	//

	private void buildScalars(GraphRegistry registry, GraphQLSchema.Builder builder, GraphQLCodeRegistry.Builder codeBuilder, RuntimeParameterFactory factory) {

		for (GraphScalarType t : registry.getScalars().all()) {
			builder.additionalType(t.built());
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
			builder.query(objectTypeDefinition);

			for (FieldFetcher fetcher : fetchers) {
				codeBuilder.dataFetcher(objectTypeDefinition, fetcher.field, fetcher.dataFetcher);
			}

		}

	}

	private void buildMutations(GraphRegistry registry, RuntimeParameterFactory factory, GraphQLSchema.Builder builder, GraphQLCodeRegistry.Builder codeBuilder) {

		List<GraphMutationField> mutations = registry.getMutations().all();
		if (!mutations.isEmpty()) {

			GraphQLObjectType.Builder objectTypeBuilder = GraphQLObjectType.newObject();
			objectTypeBuilder.name("Mutation");

			List<FieldFetcher> fetchers = new ArrayList<FieldFetcher>();

			for (GraphMutationField mutation : mutations) {
				GraphQLFieldDefinition fieldDefinition = mutation.newFieldDefinition(registry).build();
				objectTypeBuilder.field(fieldDefinition);
				fetchers.add(new FieldFetcher(fieldDefinition, mutation.getFetcher(factory)));
			}

			GraphQLObjectType objectTypeDefinition = objectTypeBuilder.build();
			builder.mutation(objectTypeDefinition);

			for (FieldFetcher fetcher : fetchers) {
				codeBuilder.dataFetcher(objectTypeDefinition, fetcher.field, fetcher.dataFetcher);
			}

		}

	}

	private void buildInterfaceTypes(GraphRegistry registry, RuntimeParameterFactory factory, GraphQLSchema.Builder builder, GraphQLCodeRegistry.Builder codeBuilder) {

		for (GraphInterfaceType interfaceType : registry.getInterfaces().all()) {

			GraphQLInterfaceType.Builder interfaceTypeBuilder = interfaceType.newInterfaceType(registry);

			// sort

			Map<String, GraphObjectTypeField> fieldsByName = interfaceType.getFields().values().stream().collect(Collectors.toMap(GraphObjectTypeField::getName, Function.identity()));
			List<GraphObjectTypeField> fields = interfaceType.getFieldOrder().stream().map(fieldsByName::remove).filter(Objects::nonNull).collect(Collectors.toList());
			fields.addAll(fieldsByName.values().stream().sorted(Comparator.comparing(GraphObjectTypeField::getName)).collect(Collectors.toList()));
			interfaceTypeBuilder.comparatorRegistry(AS_IS_REGISTRY);

			// build

			for (GraphObjectTypeField field : fields) {
				GraphQLFieldDefinition fieldDefinition = field.newFieldDefinition(registry).build();
				interfaceTypeBuilder.field(fieldDefinition);
			}

			// register

			GraphQLInterfaceType interfaceTypeDefinition = interfaceTypeBuilder.build();
			codeBuilder.typeResolver(interfaceTypeDefinition, interfaceType.getResolver());
			builder.additionalType(interfaceTypeDefinition);

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

			// interfaces

			for (GraphInterfaceType interfaceType : objectType.getInterfaces()) {
				System.out.println("INTER: " + interfaceType.getName());
				objectTypeBuilder.withInterface(typeRef(interfaceType.getName()));
			}

			// fields

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
