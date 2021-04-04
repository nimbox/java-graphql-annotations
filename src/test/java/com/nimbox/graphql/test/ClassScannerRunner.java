package com.nimbox.graphql.test;

import java.util.List;

import com.nimbox.canexer.api.utils.Entity;
import com.nimbox.graphql.scanners.ClassScanner;

public class ClassScannerRunner {

	public static void main(String[] args) {

		ClassScanner scanner = new ClassScanner();

		List<Class<?>> classes = scanner //
				.packages("com.nimbox") //
				.filter(i -> i.hasAnnotation(Entity.class.getName())) //
				.classes();

		System.out.println(classes);

	}

}
