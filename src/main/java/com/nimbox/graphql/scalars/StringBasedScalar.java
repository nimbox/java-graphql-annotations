package com.nimbox.graphql.scalars;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

public class StringBasedScalar<T> implements Coercing<T, String> {

	private final Class<T> type;
	private final ThrowingFunction<T, String> toString;
	private final ThrowingFunction<String, T> fromString;

	public StringBasedScalar(Class<T> type, ThrowingFunction<T, String> toString, ThrowingFunction<String, T> fromString) {
		this.type = type;
		this.toString = toString;
		this.fromString = fromString;
	}

	@Override
	@SuppressWarnings("unchecked")
	public String serialize(Object dataFetcherResult) throws CoercingSerializeException {

		if (type.isInstance(dataFetcherResult)) {
			try {
				return toString.apply((T) dataFetcherResult);
			} catch (Exception e) {
				throw new CoercingSerializeException(String.format("Value %s could not be serialized from a %s", dataFetcherResult, type), e);
			}
		}
		throw new CoercingSerializeException(String.format("Value %s could not be serialized from a %s", dataFetcherResult, type));

	}

	@Override
	public T parseValue(Object input) throws CoercingParseValueException {

		try {
			if (input instanceof String) {
				return fromString.apply((String) input);
			}
			throw new CoercingParseValueException(String.format("Value %s could not be parsed into a %s", input, type));
		} catch (Exception e) {
			throw new CoercingParseValueException(String.format("Value %s could not be parsed into a %s", input, type), e);
		}

	}

	@Override
	public T parseLiteral(Object input) throws CoercingParseLiteralException {
		try {
			if (input instanceof StringValue) {
				return fromString.apply(((StringValue) input).getValue());
			}
			throw new CoercingParseLiteralException(String.format("Value %s could not be parsed into a %s", input, type));
		} catch (Exception e) {
			throw new CoercingParseLiteralException(String.format("Value %s could not be parsed into a %s", input, type), e);
		}
	}

	// interfaces

	@FunctionalInterface
	public interface ThrowingFunction<T, R> {
		R apply(T t) throws Exception;
	}

}
