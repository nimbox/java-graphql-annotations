package com.nimbox.graphql.types;

import static graphql.schema.GraphqlTypeComparatorRegistry.AS_IS_REGISTRY;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.nimbox.graphql.GraphBuilderException;
import com.nimbox.graphql.annotations.GraphQLInput;
import com.nimbox.graphql.annotations.GraphQLInputField;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.utils.ReservedStrings;

public class GraphInputObjectType {

	// properties

	private final Class<?> inputObjectTypeClass;

	private final String name;
	private final String description;
	private final List<String> fieldOrder;

	private final Map<Method, GraphInputObjectTypeField> fields = new HashMap<Method, GraphInputObjectTypeField>();

	// constructors

	public GraphInputObjectType(GraphRegistry registry, final Class<?> inputObjectTypeClass) {

		GraphQLInput annotation = inputObjectTypeClass.getAnnotation(GraphQLInput.class);
		if (annotation == null) {
			annotation = inputObjectTypeClass.getAnnotatedSuperclass().getAnnotation(GraphQLInput.class);
		}
		if (annotation == null) {
			throw new GraphBuilderException(String.format("Expected annotation %s on class %s", GraphQLInput.class, inputObjectTypeClass));
		}

		// create

		this.inputObjectTypeClass = inputObjectTypeClass;

		this.name = annotation.name();
		this.description = ReservedStrings.translate(annotation.description());
		this.fieldOrder = Arrays.asList(annotation.fieldOrder());

		for (Method method : inputObjectTypeClass.getMethods()) {
			if (method.isAnnotationPresent(GraphQLInputField.class)) {
				fields.put(method, new GraphInputObjectTypeField(registry, method));
			}
		}

	}

	// getters

	public Class<?> getInputObjectTypeClass() {
		return inputObjectTypeClass;
	}

	public String getName() {
		return name;
	}

	public Optional<String> getDescription() {
		return Optional.of(description);
	}

	public List<String> getFieldOrder() {
		return fieldOrder;
	}

	public Map<Method, GraphInputObjectTypeField> getFields() {
		return fields;
	}

	// builder

	public graphql.schema.GraphQLInputObjectType.Builder newObjectType(GraphRegistry registry) {

		graphql.schema.GraphQLInputObjectType.Builder builder = graphql.schema.GraphQLInputObjectType.newInputObject();

		builder.name(name);
		if (description != null) {
			builder.description(description);
		}

		// sort by fieldsName and the rest alphabetically

		Map<String, GraphInputObjectTypeField> fieldsByName = fields.values().stream().collect(Collectors.toMap(GraphInputObjectTypeField::getName, v -> v));
		List<GraphInputObjectTypeField> fields = fieldOrder.stream().map(fieldsByName::remove).filter(Objects::nonNull).collect(Collectors.toList());
		fields.addAll(fieldsByName.values().stream().sorted(Comparator.comparing(GraphInputObjectTypeField::getName)).collect(Collectors.toList()));
		builder.comparatorRegistry(AS_IS_REGISTRY);

		// build each field

		for (GraphInputObjectTypeField field : fields) {
			
			System.out.println(field);
			
			builder.field(field.newInputObjectField(registry));
		}

		return builder;

	}

}
