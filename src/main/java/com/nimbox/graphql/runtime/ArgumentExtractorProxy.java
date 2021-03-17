package com.nimbox.graphql.runtime;

import static com.nimbox.graphql.runtime.ArgumentInputProxy.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nimbox.graphql.types.GraphInputObjectType;
import com.nimbox.graphql.types.GraphInputObjectTypeField;
import com.nimbox.graphql.types.GraphValueClass;

public class ArgumentExtractorProxy implements ArgumentExtractor {

	// properties

	private final ArgumentFactory factory;

	private final Class<?> typeClass;
	private final Map<Method, RuntimeParameter> fields = new LinkedHashMap<Method, RuntimeParameter>();

	// constructors

	public ArgumentExtractorProxy(ArgumentFactory factory, GraphInputObjectType inputObjectType) {

		this.factory = factory;

		this.typeClass = inputObjectType.getInputObjectTypeClass();
		for (Map.Entry<Method, GraphInputObjectTypeField> e : inputObjectType.getFields().entrySet()) {
			fields.put(e.getKey(), new RuntimeParameter(e.getValue().getName(), e.getValue().getValueClass()));
		}

	}

	// mehods

	@Override
	@SuppressWarnings("unchecked")
	public Object apply(Map<String, Object> arguments, RuntimeParameter parameter) throws Exception {

		String name = parameter.name;
		GraphValueClass valueClass = parameter.valueClass;

		if (!arguments.containsKey(name)) {
			if (valueClass.isList()) {
				return valueClass.undefinedList();
			} else {
				return valueClass.undefined();
			}
		}

		Object value = arguments.get(name);

		if (valueClass.isList()) {
			List<Object> list = new ArrayList<Object>(((List<?>) value).size());
			if (valueClass.isOptionalList()) {
				if (valueClass.isOptional()) {
					for (Object a : (List<?>) value) {
						list.add(valueClass.nullable(proxy(typeClass, factory, fields, (Map<String, Object>) a)));
					}
				} else {
					for (Object a : (List<?>) value) {
						list.add(proxy(typeClass, factory, fields, (Map<String, Object>) a));
					}
				}
				return valueClass.nullableList(value);
			} else {
				for (Object a : (List<?>) value) {
					list.add(proxy(typeClass, factory, fields, (Map<String, Object>) a));
				}
				return list;
			}
		} else {
			if (valueClass.isOptional()) {
				return valueClass.nullable(proxy(typeClass, factory, fields, (Map<String, Object>) value));
			} else {
				return proxy(typeClass, factory, fields, (Map<String, Object>) value);
			}
		}

	}

}