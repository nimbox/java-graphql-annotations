package com.nimbox.graphql.registries;

import static graphql.schema.GraphQLTypeReference.typeRef;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import com.nimbox.graphql.GraphBuilderException;
import com.nimbox.graphql.annotations.GraphQLField;
import com.nimbox.graphql.annotations.GraphQLType;
import com.nimbox.graphql.types.GraphEnumType;
import com.nimbox.graphql.types.GraphInputObjectType;
import com.nimbox.graphql.types.GraphInterfaceType;
import com.nimbox.graphql.types.GraphObjectType;
import com.nimbox.graphql.types.GraphOptionalDefinition;
import com.nimbox.graphql.utils.ReservedStrings;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLTypeReference;

public class GraphRegistry {

	// properties

	private final List<TypeAnnotation<?>> typeAnnotations = new ArrayList<TypeAnnotation<?>>();
	private final List<FieldAnnotation<?>> fieldAnnotations = new ArrayList<FieldAnnotation<?>>();

	//

	private final Map<Class<?>, DataFetcher<?>> contexts;
	private final Map<Class<?>, GraphOptionalDefinition<?>> optionals;

	//

	private final Set<String> names = new HashSet<String>();

	//

	private final ScalarTypeRegistry scalars;
	private final EnumTypeRegistry enums;

	private final InterfaceTypeRegistry interfaces;

	private final ObjectTypeRegistry objects;
	private final ObjectTypeExtensionRegistry objectExtensions;

	private final InputObjectTypeRegistry inputObjects;

	private final QueryRegistry queries;
	private final MutationRegistry mutations;

	// constructors

	public GraphRegistry() {
		this(null);
	}

	public GraphRegistry(Class<?> context) {

		// default annotations

		typeAnnotations.add(new TypeAnnotation<GraphQLType>( //
				GraphQLType.class, //
				GraphQLType::name, //
				a -> ReservedStrings.translate(a.description()), //
				GraphQLType::fieldOrder //
		));

		fieldAnnotations.add(new FieldAnnotation<GraphQLField>( //
				GraphQLField.class, //
				GraphQLField::name, //
				a -> ReservedStrings.translate(a.description()), //
				a -> ReservedStrings.translate(a.deprecationReason())));

		//

		this.contexts = new HashMap<Class<?>, DataFetcher<?>>();
		this.optionals = new HashMap<Class<?>, GraphOptionalDefinition<?>>();
		this.withOptional(new GraphOptionalDefinition<>(Optional.class, () -> null, Optional::ofNullable));

		this.scalars = new ScalarTypeRegistry(this);
		this.enums = new EnumTypeRegistry(this);

		this.interfaces = new InterfaceTypeRegistry(this);

		this.objects = new ObjectTypeRegistry(this);
		this.objectExtensions = new ObjectTypeExtensionRegistry(this);

		this.inputObjects = new InputObjectTypeRegistry(this);

		this.queries = new QueryRegistry(this);
		this.mutations = new MutationRegistry(this);

	}

	//
	// configuration
	//

	// object

	public GraphRegistry withTypeAnnotations(List<TypeAnnotation<?>> typeAnnotations) {
		this.typeAnnotations.addAll(typeAnnotations);
		return this;
	}

	public TypeAnnotation<?>.Content getTypeAnnotationOrThrow(Class<?> objectTypeClass) {

		for (TypeAnnotation<?> t : typeAnnotations) {
			if (objectTypeClass.isAnnotationPresent(t.annotationClass)) {
				return t.new Content(objectTypeClass.getAnnotation(t.annotationClass));
			}
		}

		throw new GraphBuilderException("");

	}

	// object field

	public GraphRegistry withFieldAnnotations(List<FieldAnnotation<?>> fieldAnnotations) {
		this.fieldAnnotations.addAll(fieldAnnotations);
		return this;
	}

	public boolean hasFieldAnnotation(Method objectFieldMethod) {

		for (FieldAnnotation<?> t : fieldAnnotations) {
			if (objectFieldMethod.isAnnotationPresent(t.annotationClass)) {
				return true;
			}
		}

		return false;

	}

	public FieldAnnotation<?>.Content getFieldAnnotationOrThrow(Method objectFieldMethod) {

		for (FieldAnnotation<?> t : fieldAnnotations) {
			if (objectFieldMethod.isAnnotationPresent(t.annotationClass)) {
				return t.new Content(objectFieldMethod.getAnnotation(t.annotationClass));
			}
		}

		throw new GraphBuilderException("");

	}

	//

	public GraphRegistry withOptional(GraphOptionalDefinition<?> optional) {
		optionals.put(optional.getKlass(), optional);
		return this;
	}

	// getters and setters

	public Map<Class<?>, DataFetcher<?>> getContexts() {
		return contexts;
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

	public InterfaceTypeRegistry getInterfaces() {
		return interfaces;
	}

	//

	public ObjectTypeRegistry getObjects() {
		return objects;
	}

	//

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

	public GraphQLOutputType getOutputType(Class<?> valueClass) {

		GraphQLOutputType scalar = getScalars().getGraphQLType(valueClass);
		if (scalar != null) {
			return scalar;
		}

		GraphObjectType objectType = objects.get(valueClass);
		if (objectType != null) {
			return typeRef(objectType.getName());
		}

		GraphInterfaceType interfaceType = interfaces.get(valueClass);
		if (interfaceType != null) {
			return typeRef(interfaceType.getName());
		}

		return null;

	}

	public GraphQLInputType getInputType(Class<?> valueClass) {

		GraphQLInputType scalar = getScalars().getGraphQLType(valueClass);
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

	//
	// annotation overrides
	//

	public static class TypeAnnotation<T extends Annotation> {

		// properties

		private final Class<T> annotationClass;

		private final Function<T, String> getName;
		private final Function<T, String> getDescription;
		private final Function<T, String[]> getFieldOrder;

		// constructors

		public TypeAnnotation(Class<T> annotationClass, Function<T, String> getName, Function<T, String> getDescription, Function<T, String[]> getFieldOrder) {
			this.annotationClass = annotationClass;
			this.getName = getName;
			this.getDescription = getDescription != null ? getDescription : a -> null;
			this.getFieldOrder = getFieldOrder != null ? getFieldOrder : a -> new String[] {};
		}

		// getters

		public class Content {

			// properties

			private final T annotation;

			// constructors

			@SuppressWarnings("unchecked")
			public Content(Annotation annotation) {
				this.annotation = (T) annotation;
			}

			// getters

			public String getName() {
				return getName.apply(annotation);
			}

			public String getDescription() {
				return getDescription.apply(annotation);
			}

			public String[] getFieldOrder() {
				return getFieldOrder.apply(annotation);
			}

		}

	}

	public static class FieldAnnotation<T extends Annotation> {

		// properties

		private final Class<T> annotationClass;

		private final Function<T, String> getName;
		private final Function<T, String> getDescription;
		private final Function<T, String> getDeprecationReason;

		// constructors

		public FieldAnnotation(Class<T> annotationClass, Function<T, String> getName, Function<T, String> getDescription, Function<T, String> getDeprecationReason) {
			this.annotationClass = annotationClass;
			this.getName = getName;
			this.getDescription = getDescription != null ? getDescription : a -> null;
			this.getDeprecationReason = getDeprecationReason != null ? getDeprecationReason : a -> null;
		}

		// getters

		public class Content {

			// properties

			private final T annotation;

			// constructors

			@SuppressWarnings("unchecked")
			public Content(Annotation annotation) {
				this.annotation = (T) annotation;
			}

			// getters

			public String getName() {
				return getName.apply(annotation);
			}

			public String getDescription() {
				return getDescription.apply(annotation);
			}

			public String getDeprecationReason() {
				return getDeprecationReason.apply(annotation);
			}

		}

	}

}
