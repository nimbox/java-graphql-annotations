package com.nimbox.graphql.test.domain;

import java.util.List;

import com.nimbox.graphql.annotations.GraphQLField;
import com.nimbox.graphql.annotations.GraphQLType;

@GraphQLType(name = "Human", description = "A humanoid creature in the Star Wars universe.", fieldOrder = { "id", "name", "homePlanet" })
public class Human extends Character implements HasPlanet {

	private String homePlanet;

	public Human(String id, String name, List<String> friends, List<Integer> appearsIn, String homePlanet) {
		super(id, name, friends, appearsIn);
		this.homePlanet = homePlanet;
	}

	@GraphQLField(name = "homePlanet", description = "The home planet of the human, or null if unknown.")
	public String getHomePlanet() {
		return homePlanet;
	}

	public void setHomePlanet(String homePlanet) {
		this.homePlanet = homePlanet;
	}

}
