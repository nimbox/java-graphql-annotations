package com.nimbox.graphql.types;

import static graphql.schema.GraphqlTypeComparatorRegistry.AS_IS_REGISTRY;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.nimbox.graphql.registries.GraphRegistry;

public class GraphInputObjectType {

	// properties

	private final Class<?> container;

	private final String name;
	private final String description;
	private final List<String> order;

	private final Map<Method, GraphInputObjectTypeField> fields = new HashMap<Method, GraphInputObjectTypeField>();

	// constructors

	public GraphInputObjectType(GraphRegistry registry, final Class<?> container, final Data data) {

		// create

		this.container = container;

		this.name = registry.name(data.getName(), container);
		this.description = data.getDescription();
		this.order = data.getOrder();

		// fields

		for (Method method : container.getMethods()) {
			if (registry.getInputObjects().acceptTypeField(container, method)) {
				fields.put(method, new GraphInputObjectTypeField(registry, container, method));
			}
		}

	}

	public GraphInputObjectType(GraphRegistry registry, final Class<?> container) {
		this(registry, container, registry.getInputObjects().extractTypeData(container));
	}

	// getters

	public Class<?> getContainer() {
		return container;
	}

	public String getName() {
		return name;
	}

	public Optional<String> getDescription() {
		return Optional.of(description);
	}

	public List<String> getOrder() {
		return order;
	}

	public Map<Method, GraphInputObjectTypeField> getFields() {
		return fields;
	}

	// builder

	public graphql.schema.GraphQLInputObjectType.Builder newObjectType(GraphRegistry registry) {

		graphql.schema.GraphQLInputObjectType.Builder builder = graphql.schema.GraphQLInputObjectType.newInputObject();

		builder.name(getName());
		getDescription().ifPresent(builder::description);

		// sort by fieldsName and the rest alphabetically

		Map<String, GraphInputObjectTypeField> fieldsByName = fields.values().stream().collect(Collectors.toMap(GraphInputObjectTypeField::getName, v -> v));
		List<GraphInputObjectTypeField> sortedFields = order.stream().map(fieldsByName::remove).filter(Objects::nonNull).collect(Collectors.toList());
		sortedFields.addAll(fieldsByName.values().stream().sorted(Comparator.comparing(GraphInputObjectTypeField::getName)).collect(Collectors.toList()));
		builder.comparatorRegistry(AS_IS_REGISTRY);

		// build each field

		for (GraphInputObjectTypeField field : sortedFields) {
			builder.field(field.newInputObjectField(registry));
		}

		return builder;

	}

	// data

	public static interface Data {

		String getName();

		String getDescription();

		List<String> getOrder();

	}

}
