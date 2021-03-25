package com.nimbox.graphql.scanners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

public class Scanner {

	// properties

	private final ClassLoader loader;
	protected final List<String> packages = new ArrayList<String>();

	protected final List<Filter> filters = new ArrayList<Filter>();

	// constructors

	public Scanner() {
		this(null);
	}

	public Scanner(ClassLoader loader) {
		this.loader = loader;
	}

	// configurations

	public Scanner packages(String... packages) {
		this.packages.addAll(Arrays.asList(packages));
		return this;
	}

	public Scanner filter(Filter... filters) {
		this.filters.addAll(Arrays.asList(filters));
		return this;
	}

	public List<Class<?>> classes() {

		List<Class<?>> classes = new ArrayList<Class<?>>();

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

		return classes;

	}

	public boolean filter(ClassInfo info) {

		if (filters.isEmpty()) {
			return true;
		}

		//

		for (Filter f : filters) {
			if (f.test(info)) {
				return true;
			}
		}
		return false;

	}

	//
	// classes
	//

	public static interface Filter extends Predicate<ClassInfo> {

	}

	public static class Packages {

		private final ClassLoader classLoader;
		private final String[] packages;

		public Packages(ClassLoader classLoader, String... packages) {
			this.classLoader = classLoader;
			this.packages = packages;
		}

		public ClassLoader getClassLoader() {
			return classLoader;
		}

		public String[] getPackages() {
			return packages;
		}

	}

}
