package com.nimbox.graphql.scalars;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import com.nimbox.graphql.annotations.GraphQLScalar;

@GraphQLScalar(type = LocalDateTime.class, name = "LocalDateTime", description = "A local date and time")
public class LocalDateTimeScalar extends StringBasedScalar<LocalDateTime> {

	// constructors

	public LocalDateTimeScalar() {
		super(LocalDateTime.class, LocalDateTimeScalar::format, LocalDateTimeScalar::parse);
	}

	// methods

	public static String format(LocalDateTime time) throws Exception {
		return ISO_INSTANT.format(time.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDateTime parse(String s) throws Exception {
		return LocalDateTime.ofInstant(ISO_INSTANT.parse(s, Instant::from).truncatedTo(ChronoUnit.MILLIS), ZoneId.systemDefault());
	}

}
