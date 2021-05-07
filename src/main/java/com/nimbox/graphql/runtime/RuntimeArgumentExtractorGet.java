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

		if (definition.isList()) {
			List<Object> list = new ArrayList<Object>(((List<?>) value).size());
			if (definition.hasOptionalList()) {
				if (definition.hasOptional()) {
					for (Object element : (List<?>) value) {
						list.add(definition.nullable(translate(definition, element)));
					}
				} else {
					for (Object element : (List<?>) value) {
						list.add(translate(definition, element));
					}
				}
				return definition.nullableList(value);
			} else {
				if (definition.hasOptional()) {
					for (Object element : (List<?>) value) {
						list.add(definition.nullable(translate(definition, element)));
					}
				} else {
					return translate(definition, value);
				}
				return list;
			}
		} else {
			if (definition.hasOptional()) {
				return definition.nullable(translate(definition, value));
			} else {
				return translate(definition, value);
			}
		}

	}

	private Object translate(GraphInputTypeDefinition definition, Object value) {

		if (value == null) {
			return null;
		}

		if (definition.isId()) {
			return definition.parseId(value);
		}

		return value;

	}

}