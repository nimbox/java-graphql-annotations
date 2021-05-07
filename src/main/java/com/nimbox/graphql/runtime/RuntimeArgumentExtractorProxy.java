package com.nimbox.graphql.runtime;

import static com.nimbox.graphql.runtime.RuntimeArgumentInputProxy.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nimbox.graphql.definitions.GraphInputTypeDefinition;
import com.nimbox.graphql.types.GraphInputObjectType;
import com.nimbox.graphql.types.GraphInputObjectTypeField;

public class RuntimeArgumentExtractorProxy implements RuntimeArgumentExtractor {

	// properties

	private final RuntimeArgumentFactory factory;

	private final Class<?> container;
	private final Map<Method, RuntimeParameterArgument> methods = new LinkedHashMap<Method, RuntimeParameterArgument>();

	// constructors

	public RuntimeArgumentExtractorProxy(final RuntimeArgumentFactory factory, final GraphInputObjectType inputObjectType) {

		this.factory = factory;

		this.container = inputObjectType.getContainer();
		for (Map.Entry<Method, GraphInputObjectTypeField> e : inputObjectType.getFields().entrySet()) {
			methods.put(e.getKey(), new RuntimeParameterArgument(e.getValue().getName(), e.getValue().getReturnInput().getDefinition()));
		}

	}

	// methods

	@Override
	@SuppressWarnings("unchecked")
	public Object apply(final Map<String, Object> arguments, final RuntimeParameter parameter) throws Exception {

		String name = parameter.name;
		GraphInputTypeDefinition definition = ((RuntimeParameterArgument) parameter).getDefinition();

		if (!arguments.containsKey(name)) {
			if (definition.isList()) {
				return definition.undefinedList();
			} else {
				return definition.undefined();
			}
		}

		Object value = arguments.get(name);

		if (definition.isList()) {
			List<Object> list = new ArrayList<Object>(((List<?>) value).size());
			if (definition.hasOptionalList()) {
				if (definition.hasOptional()) {
					for (Object element : (List<?>) value) {
						list.add(definition.nullable(proxy(container, factory, methods, (Map<String, Object>) element)));
					}
				} else {
					for (Object element : (List<?>) value) {
						list.add(proxy(container, factory, methods, (Map<String, Object>) element));
					}
				}
				return definition.nullableList(list);
			} else {
				for (Object element : (List<?>) value) {
					list.add(proxy(container, factory, methods, (Map<String, Object>) element));
				}
				return list;
			}
		} else {
			if (definition.hasOptional()) {
				return definition.nullable(proxy(container, factory, methods, (Map<String, Object>) value));
			} else {
				return proxy(container, factory, methods, (Map<String, Object>) value);
			}
		}

	}

}