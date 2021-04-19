package com.nimbox.graphql.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nimbox.graphql.definitions.GraphInputTypeDefinition;

class RuntimeArgumentExtractorGet implements RuntimeArgumentExtractor {

	// constructors

	public RuntimeArgumentExtractorGet() {
	}

	// methods

	@Override
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
		// Translate the string id to an expected type.
		if (definition.isId()) {
			value = definition.parseId(value);
		}

		if (definition.isList()) {
			List<Object> list = new ArrayList<Object>(((List<?>) value).size());
			if (definition.hasOptionalList()) {
				if (definition.hasOptional()) {
					for (Object a : (List<?>) value) {
						list.add(definition.nullable(a));
					}
				} else {
					for (Object a : (List<?>) value) {
						list.add(a);
					}
				}
				return definition.nullableList(value);
			} else {
				if (definition.hasOptional()) {
					for (Object a : (List<?>) value) {
						list.add(definition.nullable(a));
					}
				} else {
					return value;
				}
				return list;
			}
		} else {
			if (definition.hasOptional()) {
				return definition.nullable(value);
			} else {
				return value;
			}
		}

	}

}