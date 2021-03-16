package com.nimbox.graphql;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GeneratorContext {

	// properties

	private Map<Key, Object> values = new HashMap<Key, Object>();

	//

	public GeneratorContext() {
	}

	// getters and setter

	public <T> T get(Class<T> klass, String name) {
		return klass.cast(values.get(new Key(klass, name)));
	}

	public <T> void put(Class<T> klass, String name, T value) {
		values.put(new Key(klass, name), value);
	}

	// classes

	private static class Key {

		private Class<?> klass;
		private String name;

		public Key(Class<?> klass, String name) {
			this.klass = klass;
			this.name = name;
		}

		@Override
		public int hashCode() {
			return Objects.hash(klass, name);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null)
				return false;
			if (getClass() != o.getClass())
				return false;
			Key other = (Key) o;
			return Objects.equals(klass, other.klass) && Objects.equals(name, other.name);
		}

	}

}
