package com.nimbox.graphql.test;

import java.util.List;

import com.nimbox.canexer.locals.api.utils.Kind;
import com.nimbox.graphql.scanners.Scanner;

public class ClassScannerRunner {

	public static void main(String[] args) {

		Scanner scanner = new Scanner();

		List<Class<?>> classes = scanner //
				.packages("com.nimbox") //
				.filter(i -> i.hasAnnotation(Kind.class.getName())) //
				.classes();

		System.out.println(classes);

	}

}
