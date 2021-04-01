package com.nimbox.graphql.scalars;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

import java.time.LocalDateTime;

import com.nimbox.graphql.annotations.GraphQLScalar;

@GraphQLScalar(type = LocalDateTime.class, name = "LocalDateTime", description = "A local date and time")
public class LocalDateTimeScalar extends StringBasedScalar<LocalDateTime> {

	public LocalDateTimeScalar() {
		super(LocalDateTime.class, ISO_INSTANT::format, s -> ISO_INSTANT.parse(s, LocalDateTime::from));
	}

}
