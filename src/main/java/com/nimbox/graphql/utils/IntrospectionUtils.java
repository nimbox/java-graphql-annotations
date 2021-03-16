package com.nimbox.graphql.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

import com.nimbox.graphql.GeneratorException;

public class IntrospectionUtils {

	private IntrospectionUtils() {
	}

	public static Object getEnumValueFromField(Class<?> type, Field field) {

		try {
			return field.get(null);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new GeneratorException(String.format("Can't get value from field %s in class %s", field, type), e);
		}

	}

	public static <T extends Annotation> T getTypeAnnotationOrThrow(Class<T> annotation, Class<?> type) {

		T a = type.getAnnotation(annotation);
		if (a == null) {
			a = type.getAnnotatedSuperclass().getAnnotation(annotation);
		}

		if (a == null) {
			throw new GeneratorException(String.format("Class %s does not have annotation %s", type, annotation));
		}

		return a;

	}

	public static <T extends Annotation> T getAnnotationOrThrow(Class<T> annotation, AnnotatedElement element) {

		T a = element.getAnnotation(annotation);
		if (a == null) {
			throw new GeneratorException(String.format("Method %s does not have annotation %s", element, annotation));
		}

		return a;

	}

	public static <T> Constructor<T> getClassConstructorOrThrow(Class<T> type, Class<?>... parameterTypes) {

		try {
			return type.getConstructor(parameterTypes);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new GeneratorException(String.format("Class %s does not have constructor with parameters %s", type, Arrays.asList(parameterTypes)), e);
		}

	}

}
