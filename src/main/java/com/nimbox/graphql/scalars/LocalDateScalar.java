package com.nimbox.graphql.scalars;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.nimbox.graphql.annotations.GraphQLScalar;

@GraphQLScalar(type = LocalDate.class, name = "LocalDate", description = "A local date")
public class LocalDateScalar extends StringBasedScalar<LocalDate> {

	// constructors

	public LocalDateScalar() {
		super(LocalDate.class, LocalDateScalar::format, LocalDateScalar::parse);
	}

	// methods

	public static String format(LocalDate date) throws DateTimeException {
		return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
	}

	public static LocalDate parse(String s) throws DateTimeParseException {
		return LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE);
	}

}
