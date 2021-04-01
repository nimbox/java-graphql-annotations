package com.nimbox.graphql.test.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.nimbox.graphql.annotations.GraphQLArgument;
import com.nimbox.graphql.annotations.GraphQLContext;
import com.nimbox.graphql.annotations.GraphQLId;
import com.nimbox.graphql.annotations.GraphQLInput;
import com.nimbox.graphql.annotations.GraphQLInputField;
import com.nimbox.graphql.annotations.GraphQLMutation;
import com.nimbox.graphql.annotations.GraphQLQuery;
import com.nimbox.util.Alternative;

import graphql.schema.DataFetchingEnvironment;

public class HumanOperations {

	public Alternative<Human> getHuman1(@GraphQLArgument(name = "id") @GraphQLId String id) {
		return null;
	}

	public Object asd() {

		return (Object) new Character();

	}

	@GraphQLQuery(name = "getCharacter")
	public Optional<Character> getCharacter(@GraphQLArgument(name = "id") @GraphQLId String id) {

		System.out.println("in get character");

		return Optional.of(new Human(id, "Ricardo", Collections.emptyList(), Collections.emptyList(), "Guacara"));

	}

	@GraphQLQuery(name = "getHuman")
	public Optional<Human> getHuman() {

		System.out.println("in get human");

		return Optional.of(new Human("1", "Ricardo", Collections.emptyList(), Collections.emptyList(), "Guacara"));
//		return Optional.empty();

	}

	@GraphQLQuery(name = "getHumanInput")
	public Optional<Human> getHumanInput(@GraphQLArgument(name = "input") HumanInput input) {

		System.out.println("in get human");
		System.out.println(input.toString());
		System.out.println("input.getName(): " + input.getName());

		return Optional.of(new Human("1", "Ricardo", Collections.emptyList(), Collections.emptyList(), "Guacara"));
//		return Optional.empty();

	}

	@GraphQLQuery(name = "getHumans")
	public List<Human> getHumans(@GraphQLArgument(name = "limit") Integer limit) {

		System.out.println("in get humans");
		return Arrays.asList(new Human(limit.toString(), "Ricardo", Collections.emptyList(), Collections.emptyList(), "Guacara"));

	}

	@GraphQLQuery(name = "getHumansByEpisode")
	public List<Human> getHumansByEpisode(@GraphQLArgument(name = "episode") Episode episode, @GraphQLArgument(name = "age") Optional<Integer> age, @GraphQLContext DataFetchingEnvironment environment) {

		System.out.println("in get humans");
		System.out.println(episode);
		System.out.println(age);
		System.out.println((String) environment.getSource());

		return Collections.emptyList();
//		return Arrays.asList(new Human(limit.toString(), "Ricardo", Collections.emptyList(), Collections.emptyList(), "Guacara"));

	}

	@GraphQLMutation(name = "createHuman")
	public Human createHuman(@GraphQLArgument(name = "input") HumanInput input) {

		System.out.println("CREATING HUMAN...");

		System.out.println("NAME: " + input.getName());

		return new Human("007", "Joao", Collections.emptyList(), Collections.emptyList(), "Tachira");

//		return null;
	}

	// inputs

	@GraphQLInput(name = "HumanInput", order = { "name", "address" })
	public static interface HumanInput {

		@GraphQLInputField(name = "name")
		String getName();

		@GraphQLInputField(name = "address")
		Optional<String> getAddress();

//		@GraphQLInputField(name = "nested")
//		Optional<HumanNestedInput> getNested();

	}

	@GraphQLInput(name = "HumanInput", order = { "name", "address" })
	public static interface HumanNestedInput {

		@GraphQLInputField(name = "name1")
		String getName();

//		@GraphQLInputField(name = "age1")
//		Integer getAge();
//
//		@GraphQLInputField(name = "salary1")
//		Double getSalary();
//
//		@GraphQLInputField(name = "address1")
//		Optional<String> getAddress();

	}

}
