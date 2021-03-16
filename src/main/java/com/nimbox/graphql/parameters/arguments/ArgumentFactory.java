package com.nimbox.graphql.parameters.arguments;

import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;

import com.nimbox.graphql.GeneratorException;
import com.nimbox.graphql.types.GraphEnumType;
import com.nimbox.graphql.types.GraphInputObjectType;
import com.nimbox.graphql.types.GraphScalarType;
import com.nimbox.graphql.types.GraphValueClass;

public class ArgumentFactory {

	private final Map<Class<?>, Argument> scalars = new HashMap<Class<?>, Argument>();
	private final Map<Class<?>, Argument> enums = new HashMap<Class<?>, Argument>();
	private final Map<Class<?>, Argument> inputs = new HashMap<Class<?>, Argument>();

	public ArgumentFactory() {

		scalars.put(String.class, null);
		scalars.put(Double.class, null);
		scalars.put(Integer.class, null);
		scalars.put(Boolean.class, null);

	}

	// configuration

	public void withScalar(GraphScalarType scalarType) {
		scalars.put(scalarType.getScalarTypeClass(), null);
	}

	public void withEnum(GraphEnumType enumType) {
		enums.put(enumType.getEnumTypeClass(), null);
	}

	public void withInput(GraphInputObjectType input) {
		inputs.put(input.getInputObjectTypeClass(), new ArgumentInput(input));
	}

	// getters

	public <T> T of(GraphValueClass valueClass, Map<String, Object> arguments, String name) throws Exception {

		if (scalars.containsKey(valueClass.getValueClass())) {
			return (T) new ArgumentScalar().instance(this, valueClass, arguments, name);
		}

		if (enums.containsKey(valueClass.getValueClass())) {
			return (T) new ArgumentEnum().instance(this, valueClass, arguments, name);
		}

		if (inputs.containsKey(valueClass.getValueClass())) {
			return (T) inputs.get(valueClass.getValueClass()).instance(this, valueClass, arguments, name);
		}

		throw new GeneratorException(format("Unable to get argument %s of type %s", name, valueClass));

	}

}
