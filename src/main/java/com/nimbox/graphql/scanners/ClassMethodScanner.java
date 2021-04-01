package com.nimbox.graphql.scanners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.MethodInfo;
import io.github.classgraph.ScanResult;

public class ClassMethodScanner extends Scanner {

	// properties

	final List<ClassMethodFilter> filters = new ArrayList<ClassMethodFilter>();

	// constructors

	public ClassMethodScanner() {
		super();
	}

	public ClassMethodScanner(ClassLoader loader) {
		super(loader);
	}

	// configuration

	@Override
	public ClassMethodScanner packages(String... packages) {
		return (ClassMethodScanner) super.packages(packages);
	}

	public ClassMethodScanner filter(ClassMethodFilter... filters) {
		this.filters.addAll(Arrays.asList(filters));
		return this;
	}

	// methods

	@Override
	public List<Class<?>> classes() {

		List<Class<?>> classes = new ArrayList<Class<?>>();

		// setup graph

		ClassGraph graph = new ClassGraph();
		if (loader != null) {
			graph.overrideClassLoaders(loader).ignoreParentClassLoaders();
		}
		for (String p : packages) {
			graph.acceptPackages(p);
		}
		graph.enableAllInfo();

		// scan

		try (ScanResult result = graph.scan()) {
			for (ClassInfo classInfo : result.getAllClasses()) {
				for (MethodInfo methodInfo : classInfo.getMethodInfo()) {
					if (filter(classInfo, methodInfo)) {
						Class<?> c = classInfo.loadClass();
						classes.add(c);
						break;
					}
				}
			}
		}

		// return

		return classes;

	}

	private boolean filter(ClassInfo classInfo, MethodInfo methodInfo) {

		if (filters.isEmpty()) {
			return true;
		}

		for (ClassMethodFilter f : filters) {
			if (f.test(classInfo, methodInfo)) {
				return true;
			}
		}
		return false;
	}

	// classes

	public static interface ClassMethodFilter extends BiPredicate<ClassInfo, MethodInfo> {

	}

}
