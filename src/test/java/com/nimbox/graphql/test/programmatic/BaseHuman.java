package com.nimbox.graphql.test.programmatic;

import java.util.List;
import java.util.Map;

import graphql.schema.DataFetcher;

public class BaseHuman extends BaseCharacter {

	public static final BaseHuman LUKE = new BaseHuman(//
			"1000", //
			"Luke Skywalker", //
			List.of("1002", "1003", "2000", "2001"), //
			List.of(4, 5, 6), //
			"Tatooine" //
	);

	public static final BaseHuman VADER = new BaseHuman(//
			"1001", //
			"Darth Vader", //
			List.of("1004"), //
			List.of(4, 5, 6), //
			"Tatooine" //
	);

	public static final BaseHuman HAN = new BaseHuman(//
			"1002", //
			"Han Solo", //
			List.of("1000", "1003", "2001"), //
			List.of(4, 5, 6), //
			null //
	);

	public static final BaseHuman LEIA = new BaseHuman(//
			"1003", //
			"Leia Organa", //
			List.of("1000", "1002", "2000", "2001"), //
			List.of(4, 5, 6), //
			"Alderan" //
	);

	public static final BaseHuman TARKIN = new BaseHuman(//
			"1004", //
			"Wilhuff Tarkin", //
			List.of("1001"), //
			List.of(4), //
			null //
	);

	public static final Map<String, BaseHuman> HUMANS = Map.of( //
			LUKE.getId(), LUKE, //
			VADER.getId(), VADER, //
			HAN.getId(), HAN, //
			LEIA.getId(), LEIA, //
			TARKIN.getId(), TARKIN //
	);

	private String homePlanet;

	public BaseHuman(String id, String name, List<String> friends, List<Integer> appearsIn, String homePlanet) {
		super(id, name, friends, appearsIn);
		this.homePlanet = homePlanet;
	}

	public String getHomePlanet() {
		return homePlanet;
	}

	public void setHomePlanet(String homePlanet) {
		this.homePlanet = homePlanet;
	}

	// fetchers

	public static DataFetcher humanDataFetcher = environment -> {
		return HUMANS.get((String) environment.getArgument("id"));
	};

}
