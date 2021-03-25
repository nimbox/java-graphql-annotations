package com.nimbox.graphql.runtime;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class RuntimeArgumentInputProxy implements InvocationHandler {

	// properties

	private final RuntimeArgumentFactory factory;

	private final Map<Method, RuntimeParameter> fields;
	private final Map<String, Object> arguments;

	// constructors

	public RuntimeArgumentInputProxy(RuntimeArgumentFactory factory, Map<Method, RuntimeParameter> fields, Map<String, Object> arguments) {
		this.factory = factory;
		this.fields = fields;
		this.arguments = arguments;
	}

	@SuppressWarnings("unchecked")
	public static <T> T proxy(Class<?> inputObjectClass, RuntimeArgumentFactory factory, Map<Method, RuntimeParameter> fields, Map<String, Object> arguments) throws Exception {
		return (T) Proxy.newProxyInstance(inputObjectClass.getClassLoader(), new Class[] { inputObjectClass }, new RuntimeArgumentInputProxy(factory, fields, arguments));
	}

	// methods

	@Override
	public Object invoke(Object target, Method method, Object[] args) throws Throwable {

		RuntimeParameter field = fields.get(method);
		if (field != null) {
			return factory.get(arguments, field);
		}

		if (method.getName().equals("toString")) {
			return arguments.toString();
		}

		throw new IllegalArgumentException(String.format("Method %s does not have a corresponding mapping", method));

	}

}
