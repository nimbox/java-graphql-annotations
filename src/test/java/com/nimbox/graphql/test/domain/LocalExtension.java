package com.nimbox.graphql.test.domain;

import com.nimbox.canexer.api.locals.Local;
import com.nimbox.canexer.api.locals.User;
import com.nimbox.graphql.annotations.GraphQLField;
import com.nimbox.graphql.annotations.GraphQLExtension;

@GraphQLExtension(type = Local.class)
public class LocalExtension {

	private final Local local;

	// constructors

	public LocalExtension(Local local) {
		this.local = local;
	}

	// methods

	@GraphQLField(name = "createdBy")
	public User createdBy() {
		return null;
	}

	@GraphQLField(name = "updatedBy")
	public User updatedBy() {
		return null;
	}

	@GraphQLField(name = "deletedBy")
	public User deletedBy() {
		return null;
	}

}
