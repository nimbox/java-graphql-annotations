package com.nimbox.graphql.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nimbox.graphql.definitions.GraphInputTypeDefinition;

class RuntimeArgumentExtractorGet implements RuntimeArgumentExtractor {

	// constructors

	public RuntimeArgumentExtractorGet() {
	}

	// mehods

	@Override
	public Object apply(Map<String, Object> arguments, RuntimeParameter parameter) throws Exception {

		String name = parameter.name;
		GraphInputTypeDefinition valueClass = parameter.type;

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
						list.add(valueClass.nullable(a));
					}
				} else {
					for (Object a : (List<?>) value) {
						list.add(a);
					}
				}
				return valueClass.nullableList(value);
			} else {
				if (valueClass.isOptional()) {
					for (Object a : (List<?>) value) {
						list.add(valueClass.nullable(a));
					}
				} else {
					return value;
				}
				return list;
			}
		} else {
			if (valueClass.isOptional()) {
				return valueClass.nullable(value);
			} else {
				return value;
			}
		}

	}

}