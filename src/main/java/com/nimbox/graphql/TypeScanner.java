package com.nimbox.graphql;

import static graphql.schema.GraphQLEnumValueDefinition.newEnumValueDefinition;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbox.graphql.GraphBuilder.GeneratorPackage;
import com.nimbox.graphql.annotations.GraphQLEnumValue;
import com.nimbox.graphql.annotations.GraphQLType;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.scanners.Scanner;
import com.nimbox.graphql.types.GraphObjectType;
import com.nimbox.graphql.utils.ReservedStrings;

import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLEnumValueDefinition;
import graphql.schema.GraphQLObjectType;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

public class TypeScanner {

//	private static final Logger _logger = LoggerFactory.getLogger(TypeScanner.class);
//
//	// constructors
//
//	public TypeScanner(final GraphRegistry registry) {
//		super(registry);
//	}
//
//	// scan
//
//	public void scan() {
//
//		for (GeneratorPackage p : packages) {
//
//			ClassGraph graph = new ClassGraph() //
//					.acceptPackages(p.getPackages()).enableAllInfo() //
//					.ignoreParentClassLoaders() //
//					.overrideClassLoaders(p.getClassLoader());
//
//			try (ScanResult result = graph.scan()) {
//				for (ClassInfo info : result.getAllClasses()) {
//					if (info.hasAnnotation(GraphQLType.class.getName())) {
//
//						Class<?> klass = info.loadClass();
//						if (klass.isEnum()) {
//							buildEnumType(klass);
//						} else {
//							buildObjectType(klass);
//						}
//
//					}
//				}
//			}
//
//		}
//
//	}
//
//	private GraphQLEnumType buildEnumType(Class<?> klass) {
//
//		GraphQLType typeAnnotation = klass.getAnnotation(GraphQLType.class);
//		String name = typeAnnotation.name();
//		String description = typeAnnotation.description();
//
//		_logger.debug("enum {}({}) - '{}'", name, klass.getName(), description);
//
//		GraphQLEnumType.Builder enumType = GraphQLEnumType.newEnum();
//		enumType.name(name);
//		if (ReservedStrings.isDefined(description)) {
//			enumType.description(description);
//		}
//
////		registry.getEnums().add(name, klass, enumType);
//
//		Field[] fields = klass.getFields();
//		Object[] constants = klass.getEnumConstants();
//
//		for (int i = 0; i < fields.length; i++) {
//			Field field = fields[i];
//			Object constant = constants[i];
//			if (field.isAnnotationPresent(GraphQLEnumValue.class)) {
//				enumType.value(buildEnumValue(field, constant));
//			}
//		}
//
//		return enumType.build();
//
//	}
//
//	private GraphQLEnumValueDefinition buildEnumValue(Field field, Object constant) {
//
//		GraphQLEnumValue annotation = field.getAnnotation(GraphQLEnumValue.class);
//		String name = annotation.name();
//		String description = annotation.description();
//		String deprecationReasong = annotation.deprecationReason();
//
//		_logger.debug("  {}({}) - '{}'", name, constant, description);
//
//		GraphQLEnumValueDefinition.Builder enumValue = newEnumValueDefinition();
//		enumValue.name(name).value(constant);
//		if (ReservedStrings.isDefined(description)) {
//			enumValue.description(null);
//		}
//		if (ReservedStrings.isDefined(deprecationReasong)) {
//			enumValue.deprecationReason(deprecationReasong);
//		}
//
//		return enumValue.build();
//
//	}
//
//	private GraphQLObjectType buildObjectType(Class<?> type) {
//		GraphObjectType objectType = registry.getObjects().get(type);
////		return objectType.getBuilder().build();
//		return null;
//	}

}
