package com.nimbox.graphql.test.programmatic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graphql.TypeResolutionEnvironment;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;

public class BaseCharacter {

	private String id;
	private String name;
	private List<String> friends;
	private List<Integer> appearsIn;

	public BaseCharacter(String id, String name, List<String> friends, List<Integer> appearsIn) {
		super();
		this.id = id;
		this.name = name;
		this.friends = friends;
		this.appearsIn = appearsIn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

	public List<Integer> getAppearsIn() {
		return appearsIn;
	}

	public void setAppearsIn(List<Integer> appearsIn) {
		this.appearsIn = appearsIn;
	}

	// fetcher

	public static Map<String, BaseCharacter> CHARACTERS = new HashMap<String, BaseCharacter>();
	static {
		CHARACTERS.putAll(BaseHuman.HUMANS);
		CHARACTERS.putAll(BaseDroid.DROIDS);
	}

	public static TypeResolver characterTypeResolver = new TypeResolver() {

		@Override
		public GraphQLObjectType getType(TypeResolutionEnvironment environment) {
			
			String id = (String) environment.getArguments().get("id");

			if (BaseHuman.HUMANS.containsKey(id)) {
				return BaseStarWarsSchema.humanType;
			}
			if (BaseDroid.DROIDS.containsKey(id)) {
				return BaseStarWarsSchema.droidType;
			}
			return null;

		}

	};

	public static DataFetcher<List<BaseCharacter>> friendsDataFetcher = environment -> {

		List<BaseCharacter> result = new ArrayList<BaseCharacter>();
		
		for (String id : (environment.<BaseCharacter>getSource()).getFriends()) {
			result.add(CHARACTERS.get(id));
		}
		return result;

	};

}
