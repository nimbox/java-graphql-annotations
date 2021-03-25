package com.nimbox.graphql.test.domain;

import com.nimbox.graphql.annotations.GraphQLField;
import com.nimbox.graphql.annotations.GraphQLInterface;

@GraphQLInterface(name = "HasPlanet", description = "A character that has a planet.")
public interface HasPlanet {

	@GraphQLField(name = "homePlanet", description = "The home planet of the human, or null if unknown.")
	public String getHomePlanet();

}
