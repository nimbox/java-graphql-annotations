package com.nimbox.graphql.test.scalars;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Test;

import com.nimbox.graphql.scalars.LocalDateScalar;

class LocalDateTest {

	@Test
	void whenFormat_thenOK() throws Exception {
		LocalDate date = LocalDate.of(1967, 12, 19);
		assertEquals("1967-12-19", LocalDateScalar.format(date));
	}

	@Test
	void whenParse_thenOK() throws Exception {
		LocalDate date = LocalDate.of(1967, 12, 19);
		assertEquals(date, LocalDateScalar.parse("1967-12-19"));
	}

	@Test
	void whenParseWithZone_thenFail() throws Exception {
		assertThrows(DateTimeParseException.class, () -> LocalDateScalar.parse("1967-12-19Z"));
	}

}
