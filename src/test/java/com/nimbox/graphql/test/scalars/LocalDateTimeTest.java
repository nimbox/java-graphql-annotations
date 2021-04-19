package com.nimbox.graphql.test.scalars;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

class LocalDateTimeTest {

	@Test
	void when_then() {

		LocalDateTime t = LocalDateTime.now();
		System.out.println(t);
		
		t = t.truncatedTo(ChronoUnit.MILLIS);
		System.out.println(t);

		String s = ISO_INSTANT.format(t.atZone(ZoneId.systemDefault()).toInstant().truncatedTo(ChronoUnit.MILLIS));
		System.out.println(s);
		
		LocalDateTime u = LocalDateTime.ofInstant(ISO_INSTANT.parse(s, Instant::from).truncatedTo(ChronoUnit.MILLIS), ZoneId.systemDefault());
		System.out.println(u);

		assertEquals(t, u);

	}

}
