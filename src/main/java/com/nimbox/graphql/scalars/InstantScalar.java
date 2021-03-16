package com.nimbox.graphql.scalars;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

import java.time.Instant;

import com.nimbox.graphql.annotations.GraphQLScalar;

@GraphQLScalar(name = "Instant", description = "An instant in time")
public class InstantScalar extends StringBasedScalar<Instant> {

	public InstantScalar() {
		super(Instant.class, ISO_INSTANT::format, s -> ISO_INSTANT.parse(s, Instant::from));
	}

}
