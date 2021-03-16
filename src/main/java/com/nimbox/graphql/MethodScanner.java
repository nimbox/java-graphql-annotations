package com.nimbox.graphql;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbox.graphql.GraphSchemaBuilder.GeneratorPackage;
import com.nimbox.graphql.annotations.GraphQLArgument;
import com.nimbox.graphql.annotations.GraphQLContext;
import com.nimbox.graphql.annotations.GraphQLEnvironment;
import com.nimbox.graphql.annotations.GraphQLMutation;
import com.nimbox.graphql.annotations.GraphQLQuery;
import com.nimbox.graphql.annotations.GraphQLSource;
import com.nimbox.graphql.annotations.GraphQLType;
import com.nimbox.graphql.parameters.GraphParameterArgument;
import com.nimbox.graphql.parameters.GraphParameterContext;
import com.nimbox.graphql.parameters.GraphParameterEnvironment;
import com.nimbox.graphql.parameters.GraphParameterSource;
import com.nimbox.graphql.registries.GraphRegistry;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

public class MethodScanner extends Scanner<MethodScanner> {

	private static final Logger _logger = LoggerFactory.getLogger(MethodScanner.class);

	// constructors

	public MethodScanner(GraphRegistry registry) {
		super(registry);
	}

	// scan

	public void scan() {

		for (GeneratorPackage p : packages) {

			ClassGraph graph = new ClassGraph() //
					.acceptPackages(p.getPackages()) //
					.enableAllInfo() //
					.ignoreParentClassLoaders() //
					.overrideClassLoaders(p.getClassLoader());

			// scan queries

			try (ScanResult result = graph.scan()) {
				result.getClassesWithMethodAnnotation(GraphQLQuery.class.getName()).stream() //
						.filter(i -> !i.hasAnnotation(GraphQLType.class.getName())) //
						.map(ClassInfo::loadClass) //
						.forEach(this::scan);
			}

			// scan mutations

			try (ScanResult result = graph.scan()) {
				result.getClassesWithMethodAnnotation(GraphQLMutation.class.getName()).stream() //
						.filter(i -> !i.hasAnnotation(GraphQLType.class.getName())) //
						.map(ClassInfo::loadClass) //
						.forEach(this::scan);
			}

		}

	}

	public void scan(Class<?> klass) {

		for (Method method : klass.getMethods()) {

			List<Object> parameters = new ArrayList<Object>(method.getParameterCount());

//			boolean hasSource = false;
//			boolean hasEnvironment = false;
//
//			if (method.isAnnotationPresent(GraphQLQuery.class)) {
//
//				for (Parameter parameter : method.getParameters()) {
//
//					if (parameter.isAnnotationPresent(GraphQLArgument.class)) {
//						parameters.add(new GraphParameterArgument(registry, parameter));
//					}
//
//					else
//
//					if (parameter.isAnnotationPresent(GraphQLContext.class)) {
//						parameters.add(new GraphParameterContext(registry, parameter));
//					}
//
//					else
//
//					if (parameter.isAnnotationPresent(GraphQLSource.class)) {
//						if (hasSource) {
//							throw new GeneratorException("Only one " + GraphQLSource.class.getSimpleName() + " is allowed");
//						}
//						hasSource = true;
//						parameters.add(new GraphParameterSource(registry, parameter));
//					}
//
//					else
//
//					if (parameter.isAnnotationPresent(GraphQLEnvironment.class)) {
//						if (hasEnvironment) {
//							throw new GeneratorException("Only one " + GraphQLEnvironment.class.getSimpleName() + " is allowed");
//						}
//						hasEnvironment = true;
//						parameters.add(new GraphParameterEnvironment(registry, parameter));
//					}
//
//					else
//
//					{
//						throw new GeneratorException(String.format("Parameter {} does not have an annotation", parameter.getName()));
//					}
//
//				}
//
//			}

			_logger.debug("Parameters {}", parameters);

		}

	}

}
