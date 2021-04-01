package com.nimbox.graphql.test;

import java.util.List;

import com.nimbox.graphql.annotations.GraphQLQuery;
import com.nimbox.graphql.scanners.ClassMethodScanner;

public class ClassMethodScannerRunner {

	public static void main(String[] args) {

		ClassMethodScanner scanner = new ClassMethodScanner();

		List<Class<?>> classes = scanner //
				.packages("com.nimbox") //
				.filter((c, m) -> m.hasAnnotation(GraphQLQuery.class.getName())) //
				.classes();

		System.out.println(classes);

	}

}
