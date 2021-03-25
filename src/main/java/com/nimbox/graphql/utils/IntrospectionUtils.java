package com.nimbox.graphql.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nimbox.graphql.GraphBuilderException;
import com.nimbox.graphql.annotations.GraphQLInterface;

public class IntrospectionUtils {

	private IntrospectionUtils() {
	}

	public static Object getEnumValueFromField(Class<?> type, Field field) {

		try {
			return field.get(null);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new GraphBuilderException(String.format("Can't get value from field %s in class %s", field, type), e);
		}

	}

	public static <T extends Annotation> T getTypeAnnotationOrThrow(Class<T> annotation, Class<?> type) {

		T a = type.getAnnotation(annotation);
		if (a == null) {
			a = type.getAnnotatedSuperclass().getAnnotation(annotation);
		}

		if (a == null) {
			throw new GraphBuilderException(String.format("Class %s does not have annotation %s", type, annotation));
		}

		return a;

	}

	public static <T extends Annotation> T getAnnotationOrThrow(Class<T> annotation, AnnotatedElement element) {

		T a = element.getAnnotation(annotation);
		if (a == null) {
			throw new GraphBuilderException(String.format("Method %s does not have annotation %s", element, annotation));
		}

		return a;

	}

	public static <T> Constructor<T> getClassConstructorOrThrow(Class<T> type, Class<?>... parameterTypes) {

		try {
			return type.getConstructor(parameterTypes);
		} catch (Exception e) {
			throw new GraphBuilderException(String.format("Class %s does not have constructor with parameters %s", type, Arrays.asList(parameterTypes)), e);
		}

	}

	public static <T> T getClassInstanceOrThrow(Class<T> type, Class<?>... parameterTypes) {

		try {
			return (T) type.getConstructor(parameterTypes).newInstance(parameterTypes);
		} catch (Exception e) {
			throw new GraphBuilderException(String.format("Class %s does not have constructor with parameters %s", type, Arrays.asList(parameterTypes)), e);
		}

	}

	@SuppressWarnings("unused")
	public static List<Class<?>> getAllSuperclasses(Class<?> type) {

		List<Class<?>> result = new ArrayList<Class<?>>();
		for (Class<?> c = type; c != null; c = c.getSuperclass()) {
			if (c.isAnnotationPresent(GraphQLInterface.class)) {
				result.add(c);
			}
		}

		return result;

	}

	@SuppressWarnings("unused")
	public static Set<Class<?>> getAllInterfaces(Class<?> type) {

		Class<?>[] interfaces = type.getInterfaces();
		if (interfaces.length == 0) {
			return Collections.emptySet();
		}

		Set<Class<?>> result = new HashSet<Class<?>>();
		for (Class<?> i : interfaces) {
			result.add(i);
			result.addAll(getAllInterfaces(i));
		}

		return result;

	}

}
