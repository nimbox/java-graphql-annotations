package com.nimbox.graphql.test;

import com.nimbox.graphql.annotations.GraphQLQuery;
import com.nimbox.graphql.annotations.GraphQLType;

import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.MethodInfo;
import io.github.classgraph.ScanResult;

class GraphQLSchemaGenerator2Test {

	public static void main(String[] args) {

		ClassGraph graph = new ClassGraph() //
				.enableClassInfo() //
				.enableMethodInfo() //
				.enableAnnotationInfo();

		try (ScanResult result = graph.acceptPackages("com.nimbox").scan()) {

			ClassInfoList list = result.getClassesWithAnnotation(GraphQLType.class.getName());

			for (ClassInfo info : list) {

				System.out.println(info.getName());

				for (MethodInfo m : info.getMethodInfo()) {

					if (m.hasAnnotation(GraphQLQuery.class.getName())) {

						AnnotationInfo a = m.getAnnotationInfo(GraphQLQuery.class.getName());
						System.out.println("  " + m.getName());

						System.out.println("NAME: " + a.getParameterValues().get("name").getValue());
						System.out.println("DESCRIPTION: " + a.getParameterValues().get("description").getValue());

						System.out.println(a);

					}

				}

			}

		}

	}

}
