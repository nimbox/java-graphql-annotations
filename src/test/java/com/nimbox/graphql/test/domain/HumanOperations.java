package com.nimbox.graphql.test.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.nimbox.graphql.annotations.GraphQLArgument;
import com.nimbox.graphql.annotations.GraphQLId;
import com.nimbox.graphql.annotations.GraphQLInput;
import com.nimbox.graphql.annotations.GraphQLInputField;
import com.nimbox.graphql.annotations.GraphQLQuery;
import com.nimbox.util.Alternative;

public class HumanOperations {

	public Alternative<Human> getHuman1(@GraphQLArgument(name = "id") @GraphQLId String id) {
		return null;
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
	public List<Human> getHumansByEpisode(@GraphQLArgument(name = "episode") Episode episode, @GraphQLArgument(name = "age") Optional<Integer> age) {

		System.out.println("in get humans");
		System.out.println(episode);
		System.out.println(age);

		return Collections.emptyList();
//		return Arrays.asList(new Human(limit.toString(), "Ricardo", Collections.emptyList(), Collections.emptyList(), "Guacara"));

	}

	// @GraphQLMutation(name = "createHuman")
//	public Human createHuman(@GraphQLArgument(name = "input") HumanInput input) {
//		return null;
//	}

	@GraphQLInput(name = "HumanInput")
	public static interface HumanInput {

		@GraphQLInputField(name = "name")
		String getName();

	}

}
