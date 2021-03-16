package com.nimbox.graphql.parameters.arguments;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nimbox.graphql.types.GraphInputObjectTypeField;
import com.nimbox.graphql.types.GraphInputObjectType;
import com.nimbox.graphql.types.GraphValueClass;

public class ArgumentInput extends Argument {

	// properties

	private final Map<Method, ArgumentInputField> fields = new HashMap<Method, ArgumentInputField>();

	// constructors

	public ArgumentInput(GraphInputObjectType input) {

		for (Map.Entry<Method, GraphInputObjectTypeField> entry : input.getFields().entrySet()) {
			fields.put(entry.getKey(), new ArgumentInputField(entry.getValue().getName(), entry.getValue().getValueType()));
		}

	}

	// methods

	@Override
	@SuppressWarnings("unchecked")
	public Object instance(ArgumentFactory factory, final GraphValueClass valueClass, final Map<String, Object> arguments, final String name) throws Exception {

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
						list.add(valueClass.nullable(ArgumentInputProxy.of(factory, valueClass, fields, (Map<String, Object>) a)));
					}
				} else {
					for (Object a : (List<?>) value) {
						list.add(ArgumentInputProxy.of(factory, valueClass, fields, (Map<String, Object>) a));
					}
				}
				return valueClass.nullableList(value);
			} else {
				for (Object a : (List<?>) value) {
					list.add(ArgumentInputProxy.of(factory, valueClass, fields, (Map<String, Object>) a));
				}
				return list;
			}
		} else {
			if (valueClass.isOptional()) {
				return valueClass.nullable(ArgumentInputProxy.of(factory, valueClass, fields, (Map<String, Object>) value));
			} else {
				return ArgumentInputProxy.of(factory, valueClass, fields, (Map<String, Object>) value);
			}
		}

	}

}
