package com.nimbox.graphql.test.domain;

import com.nimbox.graphql.annotations.GraphQLEnum;
import com.nimbox.graphql.annotations.GraphQLEnumValue;

@GraphQLEnum(name = "Episode", description = "One of the films in the Star Wars Trilogy.")
public enum Episode {

	@GraphQLEnumValue(name = "NEWHOPE", description = "Released in 1977.")
	A_NEW_HOPE,

	@GraphQLEnumValue(name = "EMPIRE", description = "Released in 1980.")
	THE_EMPIRE_STRIKES_BACK,

	@GraphQLEnumValue(name = "JEDI", description = "Released in 1983.")
	RETURN_OF_THE_JEDI

}
