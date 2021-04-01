package com.nimbox.graphql.registries;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import com.nimbox.graphql.GraphBuilderException;
import com.nimbox.graphql.annotations.GraphQLId;
import com.nimbox.graphql.annotations.GraphQLNotNull;
import com.nimbox.graphql.definitions.GraphOptionalDefinition;

import graphql.schema.DataFetcher;

public class GraphRegistry {

	//
	// properties
	//

	private final Map<Class<?>, Map<String, DataFetcher<?>>> contexts = new HashMap<Class<?>, Map<String, DataFetcher<?>>>();

	private final List<Predicate<AnnotatedElement>> idPredicates = new ArrayList<>();
	private final List<Predicate<AnnotatedElement>> notNullPredicates = new ArrayList<>();
	private final Map<Class<?>, GraphOptionalDefinition<?>> optionals = new HashMap<Class<?>, GraphOptionalDefinition<?>>();

	// names

	private final Map<String, Class<?>> names = new HashMap<String, Class<?>>();

	// registries

	private final ScalarTypeRegistry scalars;
	private final EnumTypeRegistry enums;
	private final InputObjectTypeRegistry inputObjects;

	private final InterfaceTypeRegistry interfaces;
	private final UnionTypeRegistry unions;

	private final ObjectTypeRegistry objects;
	private final ObjectTypeExtensionRegistry objectExtensions;

	private final QueryRegistry queries;
	private final MutationRegistry mutations;

	//
	// constructors
	//

	public GraphRegistry() {

		this.withId(c -> c.isAnnotationPresent(GraphQLId.class));
		this.withNotNull(c -> c.isAnnotationPresent(GraphQLNotNull.class));
		this.withOptional(new GraphOptionalDefinition<>(Optional.class, () -> null, Optional::ofNullable));

		this.scalars = new ScalarTypeRegistry(this);
		this.enums = new EnumTypeRegistry(this);
		this.inputObjects = new InputObjectTypeRegistry(this);

		this.interfaces = new InterfaceTypeRegistry(this);
		this.unions = new UnionTypeRegistry(this);

		this.objects = new ObjectTypeRegistry(this);
		this.objectExtensions = new ObjectTypeExtensionRegistry(this);

		this.queries = new QueryRegistry(this);
		this.mutations = new MutationRegistry(this);

	}

	// configuration

	public <T> GraphRegistry withContext(Class<T> context, String name, DataFetcher<T> fetcher) {
		contexts.computeIfAbsent(context, k -> new HashMap<String, DataFetcher<?>>()).put(name, fetcher);
		return this;
	}

	public Map<Class<?>, Map<String, DataFetcher<?>>> getContexts() {
		return contexts;
	}

	//

	public GraphRegistry withId(Predicate<AnnotatedElement> predicate) {
		idPredicates.add(predicate);
		return this;
	}

	public boolean isId(AnnotatedElement element) {
		for (Predicate<AnnotatedElement> p : idPredicates) {
			if (p.test(element)) {
				return true;
			}
		}
		return false;
	}

	public GraphRegistry withNotNull(Predicate<AnnotatedElement> predicate) {
		notNullPredicates.add(predicate);
		return this;
	}

	public boolean isNotNull(AnnotatedElement element) {
		for (Predicate<AnnotatedElement> p : notNullPredicates) {
			if (p.test(element)) {
				return true;
			}
		}
		return false;
	}

	//

	public GraphRegistry withOptional(GraphOptionalDefinition<?> optional) {
		optionals.put(optional.getContainer(), optional);
		return this;
	}

	public boolean isOptional(Class<?> klass) {
		for (Class<?> optional : optionals.keySet()) {
			if (optional.isAssignableFrom(klass)) {
				return true;
			}
		}
		return false;
	}

	public boolean isList(Class<?> klass) {
		return List.class.isAssignableFrom(klass);
	}

	public GraphOptionalDefinition<?> getOptionalDefinition(Class<?> klass) {
		return optionals.get(klass);
	}

	// names

	public String name(String name, Class<?> container) {

		if (names.containsKey(name)) {
			throw new GraphBuilderException(String.format("Type %s has same name %s as %s", container, name, names.get(name)));
		}
		names.put(name, container);

		return name;

	}

	//
	// registries
	//

	public ScalarTypeRegistry getScalars() {
		return scalars;
	}

	public EnumTypeRegistry getEnums() {
		return enums;
	}

	public InputObjectTypeRegistry getInputObjects() {
		return inputObjects;
	}

	public InterfaceTypeRegistry getInterfaces() {
		return interfaces;
	}

	public UnionTypeRegistry getUnions() {
		return unions;
	}

	public ObjectTypeRegistry getObjects() {
		return objects;
	}

	public ObjectTypeExtensionRegistry getObjectExtensions() {
		return objectExtensions;
	}

	public QueryRegistry getQueries() {
		return queries;
	}

	public MutationRegistry getMutations() {
		return mutations;
	}

}
