package com.nimbox.graphql.types;

import static com.nimbox.graphql.utils.IntrospectionUtils.getAllInterfaces;
import static com.nimbox.graphql.utils.IntrospectionUtils.getAllSuperclasses;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.nimbox.graphql.registries.GraphRegistry;

public class GraphObjectType {

	// properties

	private final Class<?> container;

	private final List<GraphInterfaceType> interfaces = new ArrayList<>();
	private final List<GraphUnionType> unions = new ArrayList<>();

	private final String name;
	private final String description;
	private final List<String> order;

	private final Map<Method, GraphObjectTypeField> fields = new LinkedHashMap<>();

	// constructors

	public GraphObjectType(final GraphRegistry registry, final Class<?> container, final Data data) {

		// create

		this.container = container;

		this.name = registry.name(data.getName(), container);
		this.description = data.getDescription();
		this.order = data.getOrder();

		// extends interface

		for (Class<?> c : getAllSuperclasses(container)) {

			if (registry.getInterfaces().acceptType(c)) {
				GraphInterfaceType interfaceType = registry.getInterfaces().compute(c);
				interfaceType.addImplementation(this);
				interfaces.add(interfaceType);
			}

		}

		// implements interfaces and unions

		for (Class<?> c : getAllInterfaces(container)) {

			if (registry.getInterfaces().acceptType(c)) {
				GraphInterfaceType interfaceType = registry.getInterfaces().compute(c);
				interfaceType.addImplementation(this);
				interfaces.add(interfaceType);
			}

			if (registry.getUnions().acceptType(c)) {
				GraphUnionType unionType = registry.getUnions().compute(c);
				unionType.addImplementation(this);
				unions.add(unionType);
			}

		}

		// fields

		try {

			for (Method method : container.getMethods()) {

				System.out.println(method);

				if (registry.getObjects().acceptTypeField(container, method)) {

					if (method.toString().contains("getId()")) {
						System.out.println("has getId()");
					}

					fields.put(method, new GraphObjectTypeField(registry, container, method));
				}
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	public GraphObjectType(final GraphRegistry registry, final Class<?> container) {
		this(registry, container, registry.getObjects().extractTypeData(container));
	}

	// getters

	public Class<?> getContainer() {
		return container;
	}

	public String getName() {
		return name;
	}

	public Optional<String> getDescription() {
		return Optional.ofNullable(description);
	}

	public List<String> getOrder() {
		return order;
	}

	public Map<Method, GraphObjectTypeField> getFields() {
		return fields;
	}

	//

	public List<GraphInterfaceType> getInterfaces() {
		return interfaces;
	}

	public List<GraphUnionType> getUnions() {
		return unions;
	}

	// builder

	public graphql.schema.GraphQLObjectType.Builder newObjectType(GraphRegistry registry) {

		graphql.schema.GraphQLObjectType.Builder builder = graphql.schema.GraphQLObjectType.newObject();

		builder.name(getName());
		getDescription().ifPresent(builder::description);

		return builder;

	}

	// data

	public static interface Data {

		String getName();

		String getDescription();

		List<String> getOrder();

	}

}
