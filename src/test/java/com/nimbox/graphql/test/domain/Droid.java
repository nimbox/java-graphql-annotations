package com.nimbox.graphql.test.domain;

import java.util.List;

import com.nimbox.graphql.annotations.GraphQLField;
import com.nimbox.graphql.annotations.GraphQLType;

@GraphQLType(name = "Droid", description = "A character in the Star Wars Trilogy.")
public class Droid extends Character implements Everything {

	private String primaryFunction;

	public Droid(String id, String name, List<String> friends, List<Integer> appearsIn, String primaryFunction) {
		super(id, name, friends, appearsIn);
		this.primaryFunction = primaryFunction;
	}

	@GraphQLField(name = "primaryFunction", description = "The primary function of the droid.")
	public String getPrimaryFunction() {
		return primaryFunction;
	}

	public void setPrimaryFunction(String primaryFunction) {
		this.primaryFunction = primaryFunction;
	}

}
