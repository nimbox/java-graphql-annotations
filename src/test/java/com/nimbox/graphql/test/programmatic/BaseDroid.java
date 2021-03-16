package com.nimbox.graphql.test.programmatic;

import java.util.List;
import java.util.Map;

import graphql.schema.DataFetcher;

public class BaseDroid extends BaseCharacter {

	public static final BaseDroid THREEPIO = new BaseDroid( //
			"2000", //
			"C-3PO", //
			List.of("1000", "1002", "1003", "2001"), //
			List.of(4, 5, 6), //
			"Protocol" //
	);

	public static final BaseDroid ARTWO = new BaseDroid( //
			"2001", //
			"R2-D2", //
			List.of("1000", "1002", "1003"), //
			List.of(4, 5, 6), //
			"Astromech" //
	);

	public static final Map<String, BaseDroid> DROIDS = Map.of( //
			THREEPIO.getId(), THREEPIO, //
			ARTWO.getId(), ARTWO //
	);

	private String primaryFunction;

	public BaseDroid(String id, String name, List<String> friends, List<Integer> appearsIn, String primaryFunction) {
		super(id, name, friends, appearsIn);
		this.primaryFunction = primaryFunction;
	}

	public String getPrimaryFunction() {
		return primaryFunction;
	}

	public void setPrimaryFunction(String primaryFunction) {
		this.primaryFunction = primaryFunction;
	}

	// fetchers

	public static DataFetcher droidDataFetcher = environment -> {
		return DROIDS.get((String) environment.getArgument("id"));
	};
}
