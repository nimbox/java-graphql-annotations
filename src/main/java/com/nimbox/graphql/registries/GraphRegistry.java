package com.nimbox.graphql.registries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.nimbox.graphql.parameters.arguments.ArgumentFactory;
import com.nimbox.graphql.types.GraphEnumType;
import com.nimbox.graphql.types.GraphInputObjectType;
import com.nimbox.graphql.types.GraphObjectType;
import com.nimbox.graphql.types.GraphOptionalDefinition;

import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLTypeReference;

public class GraphRegistry {

	// properties

	private final Class<?> context;
	private final Map<Class<?>, GraphOptionalDefinition<?>> optionals;

	private final ScalarTypeRegistry scalars;
	private final EnumTypeRegistry enums;

	private final ObjectTypeRegistry objects;
	private final ObjectTypeExtensionRegistry objectExtensions;

	private final InputObjectTypeRegistry inputObjects;

	private final QueryRegistry queries;
	private final MutationRegistry mutations;

	private final ArgumentFactory argumentFactory = new ArgumentFactory();

	// constructors

	public GraphRegistry() {
		this(null);
	}

	public GraphRegistry(Class<?> context) {

		this.context = context;
		this.optionals = new HashMap<Class<?>, GraphOptionalDefinition<?>>();
		this.withOptional(new GraphOptionalDefinition<>(Optional.class, () -> null, Optional::ofNullable));

		this.scalars = new ScalarTypeRegistry(this);
		this.enums = new EnumTypeRegistry(this);

		this.objects = new ObjectTypeRegistry(this);
		this.objectExtensions = new ObjectTypeExtensionRegistry(this);

		this.inputObjects = new InputObjectTypeRegistry(this);

		this.queries = new QueryRegistry(this);
		this.mutations = new MutationRegistry(this);

	}

	// configuration

	public GraphRegistry withOptional(GraphOptionalDefinition<?> optional) {
		optionals.put(optional.getKlass(), optional);
		return this;
	}

	// getters and setters

	public Class<?> getContext() {
		return context;
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

	// getters and setters

	public ScalarTypeRegistry getScalars() {
		return scalars;
	}

	public EnumTypeRegistry getEnums() {
		return enums;
	}

	public ObjectTypeRegistry getObjects() {
		return objects;
	}

	public ObjectTypeExtensionRegistry getObjectExtensions() {
		return objectExtensions;
	}

	public InputObjectTypeRegistry getInputObjects() {
		return inputObjects;
	}

	public QueryRegistry getQueries() {
		return queries;
	}

	public MutationRegistry getMutations() {
		return mutations;
	}

	//

	public ArgumentFactory getArgumentFactory() {
		return argumentFactory;
	}

	//

	public GraphQLOutputType getOutputType(Class<?> valueClass) {

		GraphQLOutputType scalar = getScalars().getScalarType(valueClass);
		if (scalar != null) {
			return scalar;
		}

		GraphObjectType objectType = objects.get(valueClass);
		if (objectType != null) {
			return GraphQLTypeReference.typeRef(objectType.getName());
		}

		return null;

	}

	public GraphQLInputType getInputType(Class<?> valueClass) {

		GraphQLInputType scalar = getScalars().getScalarType(valueClass);
		if (scalar != null) {
			return scalar;
		}

		GraphEnumType enumType = getEnums().get(valueClass);
		if (enumType != null) {
			return GraphQLTypeReference.typeRef(enumType.getName());
		}

		GraphInputObjectType inputObjectType = inputObjects.get(valueClass);
		if (inputObjectType != null) {
			return GraphQLTypeReference.typeRef(inputObjectType.getName());
		}

		return null;

	}

}
