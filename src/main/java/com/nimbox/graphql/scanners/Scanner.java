package com.nimbox.graphql.scanners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class Scanner {

	// properties

	final ClassLoader loader;
	final List<String> packages = new ArrayList<String>();

	// constructors

	Scanner() {
		this(null);
	}

	Scanner(ClassLoader loader) {
		this.loader = loader;
	}

	// configurations

	public Scanner packages(String... packages) {
		this.packages.addAll(Arrays.asList(packages));
		return this;
	}

	// methods

	public abstract List<Class<?>> classes();

	// classes

	static class Packages {

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
