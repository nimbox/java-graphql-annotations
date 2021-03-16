package com.nimbox.graphql;

import java.util.ArrayList;
import java.util.List;

import com.nimbox.graphql.GraphSchemaBuilder.GeneratorPackage;
import com.nimbox.graphql.registries.GraphRegistry;

public class Scanner<T> {

	// properties

	protected final GraphRegistry registry;

	protected List<GeneratorPackage> packages = new ArrayList<GeneratorPackage>();

	// constructors

	public Scanner(final GraphRegistry registry) {
		this.registry = registry;
	}

	// configurations

	public Scanner<T> withPackages(ClassLoader classLoader, String... packages) {
		this.packages.add(new GeneratorPackage(classLoader, packages));
		return this;
	}
	//

	//
	// classes
	//

	public static class ScannerPackage {

		private final ClassLoader classLoader;
		private final String[] packages;

		public ScannerPackage(ClassLoader classLoader, String... packages) {
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
