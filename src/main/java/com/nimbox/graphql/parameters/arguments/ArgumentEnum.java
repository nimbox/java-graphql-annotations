package com.nimbox.graphql.parameters.arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nimbox.graphql.types.GraphValueClass;

public class ArgumentEnum extends Argument {

	@Override
	public Object instance(ArgumentFactory factory, GraphValueClass valueClass, Map<String, Object> arguments, String name) throws Exception {

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