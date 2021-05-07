package com.nimbox.graphql;

import static graphql.schema.GraphQLTypeReference.typeRef;
import static graphql.schema.GraphqlTypeComparatorRegistry.AS_IS_REGISTRY;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.nimbox.graphql.definitions.GraphOptionalDefinition;
import com.nimbox.graphql.registries.ClassExtractor;
import com.nimbox.graphql.registries.ClassFieldExtractor;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.registries.GraphRegistry.NotNullPredicate;
import com.nimbox.graphql.registries.IdCoercing;
import com.nimbox.graphql.registries.IdCoercing.ParseFunction;
import com.nimbox.graphql.registries.IdCoercing.SerializeFunction;
import com.nimbox.graphql.runtime.RuntimeParameterFactory;
import com.nimbox.graphql.types.GraphEnumType;
import com.nimbox.graphql.types.GraphEnumTypeValue;
import com.nimbox.graphql.types.GraphInputObjectType;
import com.nimbox.graphql.types.GraphInputObjectTypeField;
import com.nimbox.graphql.types.GraphInterfaceType;
import com.nimbox.graphql.types.GraphInterfaceTypeExtension;
import com.nimbox.graphql.types.GraphInterfaceTypeField;
import com.nimbox.graphql.types.GraphMutationField;
import com.nimbox.graphql.types.GraphObjectType;
import com.nimbox.graphql.types.GraphObjectTypeExtension;
import com.nimbox.graphql.types.GraphObjectTypeField;
import com.nimbox.graphql.types.GraphQueryField;
import com.nimbox.graphql.types.GraphScalarType;
import com.nimbox.graphql.types.GraphTypeExtensionField;
import com.nimbox.graphql.types.GraphUnionType;

import graphql.schema.Coercing;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLCodeRegistry;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLUnionType;

public class GraphBuilder {

	//
	// properties
	//

	private Map<Class<?>, Map<String, DataFetcher<?>>> contexts = new HashMap<>();

	private final List<Predicate<AnnotatedElement>> idExtractors = new ArrayList<>();
	private final Map<Class<?>, IdCoercing<?>> idCoercings = new HashMap<>();

	private final List<NotNullPredicate> notNullExtractors = new ArrayList<>();
	private List<GraphOptionalDefinition<?>> optionals = new ArrayList<>();

	private final List<ClassExtractor<Class<?>, GraphInterfaceType.Data>> interfaceExtractors = new ArrayList<>();
	private final List<ClassFieldExtractor<Class<?>, Method, GraphInterfaceTypeField.Data>> interfaceFieldExtractors = new ArrayList<>();

	private final List<ClassExtractor<Class<?>, GraphObjectType.Data>> objectExtractors = new ArrayList<>();
	private final List<ClassFieldExtractor<Class<?>, Method, GraphObjectTypeField.Data>> objectFieldExtractors = new ArrayList<>();

	private final List<ClassExtractor<Class<?>, GraphUnionType.Data>> unionExtractors = new ArrayList<>();

	//

	private List<Class<? extends Coercing<?, ?>>> scalars = new ArrayList<>();
	private List<Class<?>> enums = new ArrayList<>();
	private List<Class<?>> inputObjects = new ArrayList<>();

	private List<Class<?>> interfaces = new ArrayList<>();
	private List<Class<?>> objects = new ArrayList<>();
	private List<Class<?>> extensions = new ArrayList<>();

	private List<Class<?>> operations = new ArrayList<>();

	//
	// constructors
	//

	public GraphBuilder() {
	}

	//
	// configurators
	//

	// context

	public final <T> GraphBuilder withContext(Class<T> context, DataFetcher<T> fetcher) {
		return withContext(context, null, fetcher);
	}

	public final <T> GraphBuilder withContext(Class<T> context, String name, DataFetcher<T> fetcher) {
		contexts.computeIfAbsent(context, k -> new HashMap<String, DataFetcher<?>>()).put(name, fetcher);
		return this;
	}

	// ids

	@SafeVarargs
	public final GraphBuilder withIdExtractors(Predicate<AnnotatedElement>... predicates) {
		return withIdExtractors(Arrays.asList(predicates));
	}

	public final GraphBuilder withIdExtractors(List<Predicate<AnnotatedElement>> predicates) {
		this.idExtractors.addAll(predicates);
		return this;
	}

	public final <T> GraphBuilder withIdCoercing(final Class<T> type, final SerializeFunction<T> serialize, final ParseFunction<T> parse) {
		idCoercings.put(type, new IdCoercing<T>(type, serialize, parse));
		return this;
	}

	// not nulls

	@SafeVarargs
	public final GraphBuilder withNotNullExtractors(NotNullPredicate... predicates) {
		return withNotNull(Arrays.asList(predicates));
	}

	public final GraphBuilder withNotNull(List<NotNullPredicate> predicates) {
		this.notNullExtractors.addAll(predicates);
		return this;
	}

	// optionals

	public final <U> GraphBuilder withOptional(Class<U> klass, Supplier<U> undefined, Function<Object, U> nullable) {
		this.optionals.add(new GraphOptionalDefinition<>(klass, undefined, nullable));
		return this;
	}

	// scalars

	@SafeVarargs
	public final GraphBuilder withScalars(Class<? extends Coercing<?, ?>>... scalars) {
		return withScalars(Arrays.asList(scalars));
	}

	public final GraphBuilder withScalars(List<Class<? extends Coercing<?, ?>>> scalars) {
		this.scalars.addAll(scalars);
		return this;
	}

	// enums

	public final GraphBuilder withEnums(Class<?>... enums) {
		return withEnums(Arrays.asList(enums));
	}

	public final GraphBuilder withEnums(List<Class<?>> enums) {
		this.enums.addAll(enums);
		return this;
	}

	// input objects

	public final GraphBuilder withInputObjects(Class<?>... inputObjects) {
		return withInputObjects(Arrays.asList(inputObjects));
	}

	public final GraphBuilder withInputObjects(List<Class<?>> inputObjects) {
		this.inputObjects.addAll(inputObjects);
		return this;
	}

	// interfaces

	public GraphBuilder withInterfaceExtractor(Predicate<Class<?>> is, Function<Class<?>, GraphInterfaceType.Data> extractor) {
		interfaceExtractors.add(new ClassExtractor<>(is, extractor));
		return this;
	}

	public GraphBuilder withInterfaceFieldExtractor(BiPredicate<Class<?>, Method> accept, BiFunction<Class<?>, Method, GraphInterfaceTypeField.Data> extract) {
		interfaceFieldExtractors.add(new ClassFieldExtractor<>(accept, extract));
		return this;
	}

	public GraphBuilder withInterfaces(Class<?>... interfaces) {
		return withObjects(Arrays.asList(interfaces));
	}

	public GraphBuilder withInterfaces(List<Class<?>> interfaces) {
		this.interfaces.addAll(interfaces);
		return this;
	}

	// objects

	public GraphBuilder withObjectExtractor(Predicate<Class<?>> is, Function<Class<?>, GraphObjectType.Data> extractor) {
		objectExtractors.add(new ClassExtractor<>(is, extractor));
		return this;
	}

	public GraphBuilder withObjectFieldExtractor(BiPredicate<Class<?>, Method> accept, BiFunction<Class<?>, Method, GraphObjectTypeField.Data> extract) {
		objectFieldExtractors.add(new ClassFieldExtractor<>(accept, extract));
		return this;
	}

	public GraphBuilder withObjects(Class<?>... objects) {
		return withObjects(Arrays.asList(objects));
	}

	public GraphBuilder withObjects(List<Class<?>> objects) {
		this.objects.addAll(objects);
		return this;
	}

	// object and interface extensions

	public GraphBuilder withExtensions(Class<?>... extensions) {
		return withExtensions(Arrays.asList(extensions));
	}

	public GraphBuilder withExtensions(List<Class<?>> extensions) {

		System.out.println("ADDING EXTENSIONS...");
		extensions.forEach(System.out::println);

		this.extensions.addAll(extensions);
		return this;

	}

	// unions

	public GraphBuilder withUnionExtractor(Predicate<Class<?>> is, Function<Class<?>, GraphUnionType.Data> extractor) {
		unionExtractors.add(new ClassExtractor<>(is, extractor));
		return this;
	}

	// operations

	public final GraphBuilder withOperations(Class<?>... operations) {
		return withOperations(Arrays.asList(operations));
	}

	public GraphBuilder withOperations(List<Class<?>> operations) {
		this.operations.addAll(operations);
		return this;
	}

	//
	// build
	//

	@SuppressWarnings("unchecked")
	private GraphRegistry buildRegistry() {

		GraphRegistry registry = new GraphRegistry();

		// configure

		contexts.forEach((c, m) -> m.forEach((n, f) -> registry.withContext((Class<Object>) c, n, (DataFetcher<Object>) f)));

		registry.getInterfaces().withExtractors(interfaceExtractors);
		registry.getInterfaces().withFieldExtractors(interfaceFieldExtractors);

		registry.getObjects().withExtractors(objectExtractors);
		registry.getObjects().withFieldExtractors(objectFieldExtractors);

		idExtractors.forEach(registry::withIdExtractor);
		idCoercings.values().forEach(registry::withIdCoercing);

		notNullExtractors.forEach(registry::withNotNullExtractor);
		optionals.forEach(registry::withOptional);

		//

		scalars.stream().forEach(registry.getScalars()::compute);
		enums.stream().forEach(registry.getEnums()::compute);

		objects.stream().forEach(registry.getObjects()::compute);
		extensions.stream().filter(registry.getObjectExtensions()::isAcceptable).forEach(registry.getObjectExtensions()::compute);

		extensions.stream().filter(registry.getInterfaceExtensions()::isAcceptable).forEach(registry.getInterfaceExtensions()::compute);

		operations.stream().forEach(registry.getQueries()::compute);
		operations.stream().forEach(registry.getMutations()::compute);

		// return

		return registry;

	}

	@SuppressWarnings("unchecked")
	public GraphQLSchema build() {

		GraphRegistry registry = buildRegistry();

		// build

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
		buildUnionTypes(registry, factory, builder, codeBuilder);

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

	public String buildTypescript() {

		GraphRegistry registry = buildRegistry();

		StringBuilder builder = new StringBuilder();

		for (GraphInterfaceType interfaceType : registry.getInterfaces().all()) {

			builder.append("interface ").append(interfaceType.getName());

			if (!interfaceType.getInterfaces().isEmpty()) {
				builder.append(" extends ");
				builder.append(interfaceType.getInterfaces().stream().map(GraphInterfaceType::getName).collect(Collectors.joining(", ")));
			}
			builder.append(" {").append("\n");

			for (GraphInterfaceTypeField field : interfaceType.getFields().values()) {
				builder.append("    ").append(field.getName()).append(": ");
				builder.append(field.getReturn().getDefinition().getType().getSimpleName());
				builder.append("\n");
			}

			builder.append("}").append("\n");

		}

		for (GraphObjectType objectType : registry.getObjects().all()) {

			builder.append("interface ").append(objectType.getName());

			if (!objectType.getInterfaces().isEmpty()) {
				builder.append(" extends ");
				builder.append(objectType.getInterfaces().stream().map(GraphInterfaceType::getName).collect(Collectors.joining(", ")));
			}
			builder.append(" {").append("\n");

			for (GraphObjectTypeField field : objectType.getFields().values()) {
				builder.append("    ").append(field.getName()).append(": ");
				builder.append(field.getReturn().getDefinition().getType().getSimpleName());
				builder.append("\n");
			}

			builder.append("}").append("\n");

		}

		for (GraphInputObjectType inputObject : registry.getInputObjects().all()) {

			builder.append("interface ").append(inputObject.getName());
			builder.append(" {").append("\n");

			for (GraphInputObjectTypeField field : inputObject.getFields().values()) {
				builder.append("    ").append(field.getName()).append(": ");
				builder.append(field.getReturnInput().getDefinition().getType().getSimpleName());
				builder.append("\n");
			}

			builder.append("}").append("\n");

		}

		return builder.toString();

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

	private void buildInterfaceTypes(GraphRegistry registry, RuntimeParameterFactory factory, GraphQLSchema.Builder builder, GraphQLCodeRegistry.Builder codeBuilder) {

		for (GraphInterfaceType interfaceType : registry.getInterfaces().all()) {

			GraphQLInterfaceType.Builder interfaceTypeBuilder = interfaceType.newInterfaceType(registry);

			// sort

			Map<String, GraphInterfaceTypeField> fieldsByName = interfaceType.getFields().values().stream().collect(Collectors.toMap(GraphInterfaceTypeField::getName, Function.identity()));
			List<GraphInterfaceTypeField> fields = interfaceType.getOrder().stream().map(fieldsByName::remove).filter(Objects::nonNull).collect(Collectors.toList());
			fields.addAll(fieldsByName.values().stream().sorted(Comparator.comparing(GraphInterfaceTypeField::getName)).collect(Collectors.toList()));
			interfaceTypeBuilder.comparatorRegistry(AS_IS_REGISTRY);

			// interfaces

			for (GraphInterfaceType implementInterfaceType : interfaceType.getInterfaces()) {
				interfaceTypeBuilder.withInterface(typeRef(implementInterfaceType.getName()));
				for (GraphInterfaceTypeExtension extension : registry.getInterfaceExtensions().getForType(implementInterfaceType.getContainer())) {
					for (GraphTypeExtensionField field : extension.getFields().values()) {
						GraphQLFieldDefinition fieldDefinition = field.newFieldDefinition(registry).build();
						interfaceTypeBuilder.field(fieldDefinition);
					}
				}
			}

			// build

			for (GraphInterfaceTypeField field : fields) {
				GraphQLFieldDefinition fieldDefinition = field.newFieldDefinition(registry).build();
				interfaceTypeBuilder.field(fieldDefinition);
			}

			// extend

			for (GraphInterfaceTypeExtension extension : registry.getInterfaceExtensions().getForType(interfaceType.getContainer())) {
				for (GraphTypeExtensionField field : extension.getFields().values()) {
					GraphQLFieldDefinition fieldDefinition = field.newFieldDefinition(registry).build();
					interfaceTypeBuilder.field(fieldDefinition);
				}
			}

			// register

			GraphQLInterfaceType interfaceTypeDefinition = interfaceTypeBuilder.build();
			codeBuilder.typeResolver(interfaceTypeDefinition, interfaceType.getTypeResolver());
			builder.additionalType(interfaceTypeDefinition);

		}

	}

	private void buildObjectTypes(GraphRegistry registry, RuntimeParameterFactory factory, GraphQLSchema.Builder builder, GraphQLCodeRegistry.Builder codeBuilder) {

		for (GraphObjectType objectType : registry.getObjects().all()) {

			GraphQLObjectType.Builder objectTypeBuilder = objectType.newObjectType(registry);
			List<FieldFetcher> fetchers = new ArrayList<FieldFetcher>();

			// sort

			Map<String, GraphObjectTypeField> fieldsByName = objectType.getFields().values().stream().collect(Collectors.toMap(GraphObjectTypeField::getName, Function.identity()));
			List<GraphObjectTypeField> fields = objectType.getOrder().stream().map(fieldsByName::remove).filter(Objects::nonNull).collect(Collectors.toList());
			fields.addAll(fieldsByName.values().stream().sorted(Comparator.comparing(GraphObjectTypeField::getName)).collect(Collectors.toList()));
			objectTypeBuilder.comparatorRegistry(AS_IS_REGISTRY);

			// interfaces

			for (GraphInterfaceType interfaceType : objectType.getInterfaces()) {
				objectTypeBuilder.withInterface(typeRef(interfaceType.getName()));
				for (GraphInterfaceTypeExtension extension : registry.getInterfaceExtensions().getForType(interfaceType.getContainer())) {
					for (GraphTypeExtensionField field : extension.getFields().values()) {
						GraphQLFieldDefinition fieldDefinition = field.newFieldDefinition(registry).build();
						objectTypeBuilder.field(fieldDefinition);
						fetchers.add(new FieldFetcher(fieldDefinition, field.getDataFetcher(factory)));
					}
				}
			}

			// fields

			for (GraphObjectTypeField field : fields) {
				GraphQLFieldDefinition fieldDefinition = field.newFieldDefinition(registry).build();
				objectTypeBuilder.field(fieldDefinition);
				fetchers.add(new FieldFetcher(fieldDefinition, field.getDataFetcher(factory)));
			}

			// extend

			for (GraphObjectTypeExtension extension : registry.getObjectExtensions().getForType(objectType.getContainer())) {
				for (GraphTypeExtensionField field : extension.getFields().values()) {
					GraphQLFieldDefinition fieldDefinition = field.newFieldDefinition(registry).build();
					objectTypeBuilder.field(fieldDefinition);
					fetchers.add(new FieldFetcher(fieldDefinition, field.getDataFetcher(factory)));
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

	private void buildUnionTypes(GraphRegistry registry, RuntimeParameterFactory factory, GraphQLSchema.Builder builder, GraphQLCodeRegistry.Builder codeBuilder) {

		for (GraphUnionType unionType : registry.getUnions().all()) {

			GraphQLUnionType.Builder unionTypeBuilder = unionType.newUnionType(registry);

			// build

			for (GraphObjectType objectType : unionType.getImplementations()) {
				unionTypeBuilder.possibleType(registry.getObjects().getGraphQLType(objectType.getContainer()));
			}

			// register

			GraphQLUnionType unionTypeDefinition = unionTypeBuilder.build();
			codeBuilder.typeResolver(unionTypeDefinition, unionType.getTypeResolver());
			builder.additionalType(unionTypeDefinition);

		}

	}

	private void buildQueries(GraphRegistry registry, RuntimeParameterFactory factory, GraphQLSchema.Builder builder, GraphQLCodeRegistry.Builder codeBuilder) {

		Collection<GraphQueryField> queries = registry.getQueries().all();
		if (!queries.isEmpty()) {

			GraphQLObjectType.Builder objectTypeBuilder = GraphQLObjectType.newObject();
			objectTypeBuilder.name("Query");

			List<FieldFetcher> fetchers = new ArrayList<FieldFetcher>();

			for (GraphQueryField query : queries) {
				GraphQLFieldDefinition fieldDefinition = query.newFieldDefinition(registry).build();
				objectTypeBuilder.field(fieldDefinition);
				fetchers.add(new FieldFetcher(fieldDefinition, query.getDataFetcher(factory)));
			}

			GraphQLObjectType objectTypeDefinition = objectTypeBuilder.build();
			builder.query(objectTypeDefinition);

			for (FieldFetcher fetcher : fetchers) {
				codeBuilder.dataFetcher(objectTypeDefinition, fetcher.field, fetcher.dataFetcher);
			}

		}

	}

	private void buildMutations(GraphRegistry registry, RuntimeParameterFactory factory, GraphQLSchema.Builder builder, GraphQLCodeRegistry.Builder codeBuilder) {

		Collection<GraphMutationField> mutations = registry.getMutations().all();
		if (!mutations.isEmpty()) {

			GraphQLObjectType.Builder objectTypeBuilder = GraphQLObjectType.newObject();
			objectTypeBuilder.name("Mutation");

			List<FieldFetcher> fetchers = new ArrayList<FieldFetcher>();

			for (GraphMutationField mutation : mutations) {
				GraphQLFieldDefinition fieldDefinition = mutation.newFieldDefinition(registry).build();
				objectTypeBuilder.field(fieldDefinition);
				fetchers.add(new FieldFetcher(fieldDefinition, mutation.getDataFetcher(factory)));
			}

			GraphQLObjectType objectTypeDefinition = objectTypeBuilder.build();
			builder.mutation(objectTypeDefinition);

			for (FieldFetcher fetcher : fetchers) {
				codeBuilder.dataFetcher(objectTypeDefinition, fetcher.field, fetcher.dataFetcher);
			}

		}

	}

	//
	// classes
	//

	private static class FieldFetcher {

		private final GraphQLFieldDefinition field;
		private final DataFetcher<?> dataFetcher;

		public FieldFetcher(GraphQLFieldDefinition field, DataFetcher<?> dataFetcher) {
			this.field = field;
			this.dataFetcher = dataFetcher;
		}

	}

}
