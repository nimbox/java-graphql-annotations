package com.nimbox.graphql.registries;

import java.util.function.Function;

import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

public class IdCoercing<T> {

	// properties

	private final Class<T> type;

	private final SerializeFunction<T> serialize;
	private final ParseFunction<T> parse;

	// constructors

	public IdCoercing(final Class<T> type, final SerializeFunction<T> serialize, final ParseFunction<T> parse) {

		this.type = type;

		this.serialize = serialize;
		this.parse = parse;

	}

	// methods

	public Class<T> getType() {
		return type;
	}

	public String serialize(T t) throws CoercingSerializeException {
		return serialize.apply(t);
	}

	public T parse(String s) throws CoercingParseValueException {
		return parse.apply(s);
	}

	// interfaces

	public interface SerializeFunction<T> extends Function<T, String> {
		String apply(T t) throws CoercingSerializeException;
	}

	public interface ParseFunction<T> extends Function<String, T> {
		T apply(String s) throws CoercingParseValueException;
	}

}
