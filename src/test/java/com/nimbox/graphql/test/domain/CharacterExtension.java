package com.nimbox.graphql.test.domain;

import java.util.Collections;
import java.util.List;

import com.nimbox.graphql.annotations.GraphQLQuery;
import com.nimbox.graphql.annotations.GraphQLTypeExtension;

@GraphQLTypeExtension(type = Human.class)
public class CharacterExtension {

	private final Human human;

	public CharacterExtension(Human human) {
		this.human = human;
	}

	@GraphQLQuery(name = "getFriends", description = "The friends of the character, or an empty list if they have none.")
	public List<Human> getFriends() {

		System.out.println("human.getId(): " + human.getId().getClass());
		
		return List.of(new Human(human.getId(), "Alguien", Collections.emptyList(), Collections.emptyList(), "TATOOINE"));

	}

//	@GraphQLQuery(name = "appearsIn", description = "Which movies they appear in.")
//	public List<Episode> getAppearsIn(@GraphQLArgument(name = "age") Optional<Integer> age) {
//		return Collections.emptyList();
//	}

}
