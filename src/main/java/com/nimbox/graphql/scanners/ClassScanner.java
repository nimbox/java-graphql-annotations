package com.nimbox.graphql.scanners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

public class ClassScanner extends Scanner {

	// properties

	final List<ClassFilter> filters = new ArrayList<ClassFilter>();

	// constructors

	public ClassScanner() {
		super();
	}

	public ClassScanner(ClassLoader loader) {
		super(loader);
	}

	// configuration

	@Override
	public ClassScanner packages(String... packages) {
		return (ClassScanner) super.packages(packages);
	}

	public ClassScanner filter(ClassFilter... filters) {
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
			for (ClassInfo info : result.getAllClasses()) {
				if (filter(info)) {
					Class<?> c = info.loadClass();
					classes.add(c);
				}
			}
		}

		// return

		return classes;

	}

	private boolean filter(ClassInfo info) {

		if (filters.isEmpty()) {
			return true;
		}

		//

		for (ClassFilter f : filters) {
			if (f.test(info)) {
				return true;
			}
		}
		return false;

	}

	// classes

	public static interface ClassFilter extends Predicate<ClassInfo> {

	}

}
