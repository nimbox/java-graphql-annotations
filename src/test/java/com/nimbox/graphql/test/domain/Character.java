package com.nimbox.graphql.test.domain;

import java.util.List;

import com.nimbox.graphql.annotations.GraphQLField;
import com.nimbox.graphql.annotations.GraphQLId;
import com.nimbox.graphql.annotations.GraphQLInterface;

@GraphQLInterface(name = "Character", description = "A character in the Star Wars Trilogy.")
public class Character {

	private String id;
	private String name;

	public Character() {
	}

	public Character(String id, String name, List<String> friends, List<Integer> appearsIn) {
		this.id = id;
		this.name = name;
	}

	@GraphQLField(name = "id", description = "The id of the character.")
	public @GraphQLId String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@GraphQLField(name = "name", description = "The name of the character.")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
