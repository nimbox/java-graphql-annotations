package com.nimbox.graphql.parameters.arguments;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import com.nimbox.graphql.types.GraphValueClass;

public class ArgumentInputProxy implements InvocationHandler {

	// properties

	private final ArgumentFactory factory;
	private final Map<Method, ArgumentInputField> fields;
	private final Map<String, Object> arguments;

	// constructors

	public ArgumentInputProxy(ArgumentFactory factory, Map<Method, ArgumentInputField> fields, Map<String, Object> arguments) {
		this.factory = factory;
		this.fields = fields;
		this.arguments = arguments;
	}

	@SuppressWarnings("unchecked")
	public static <T> T of(ArgumentFactory factory, GraphValueClass type, Map<Method, ArgumentInputField> fields, Map<String, Object> arguments) throws Exception {
		return (T) Proxy.newProxyInstance(type.getValueClass().getClassLoader(), new Class[] { type.getValueClass() }, new ArgumentInputProxy(factory, fields, arguments));
	}

	// methods

	@Override
	public Object invoke(Object target, Method method, Object[] args) throws Throwable {

		ArgumentInputField field = fields.get(method);
		if (field != null) {
			return factory.of(field.getReturnType(), arguments, field.getName());
		}

		if (method.getName().equals("toString")) {
			return arguments.toString();
		}

		throw new IllegalArgumentException(String.format("Method %s does not have a mapping", method));

	}

}
