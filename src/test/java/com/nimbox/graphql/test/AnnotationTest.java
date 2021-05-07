package com.nimbox.graphql.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nimbox.graphql.annotations.GraphQLId;
import com.nimbox.graphql.annotations.GraphQLNotNull;
import com.nimbox.graphql.definitions.GraphWrappedTypeDefinition;
import com.nimbox.graphql.definitions.GraphOptionalDefinition;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.util.Alternative;

class AnnotationTest {

	GraphRegistry registry;

	@BeforeEach
	void beforeEach() {
		registry = new GraphRegistry();
		registry.withOptional(new GraphOptionalDefinition<>(Alternative.class, Alternative::undefined, Alternative::ofNullable));
		System.out.println("---");
	}

	// scalar

	@Test
	void whenReturnScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public String method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(false, d.isId());
		assertEquals(false, d.isNotNull());
		assertEquals(null, d.getOptionalDefinition());

		assertEquals(null, d.getListType());
		assertEquals(false, d.isListNotNull());
		assertEquals(null, d.getOptionalListDefinition());

	}

	@Test
	void whenReturnIdScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public @GraphQLId String method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(true, d.isId());
		assertEquals(false, d.isNotNull());
		assertEquals(null, d.getOptionalDefinition());

		assertEquals(null, d.getListType());
		assertEquals(false, d.isListNotNull());
		assertEquals(null, d.getOptionalListDefinition());

	}

	@Test
	void whenReturnNotNullIdScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public @GraphQLNotNull @GraphQLId String method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(true, d.isId());
		assertEquals(true, d.isNotNull());
		assertEquals(null, d.getOptionalDefinition());

		assertEquals(null, d.getListType());
		assertEquals(false, d.isListNotNull());
		assertEquals(null, d.getOptionalListDefinition());

	}

	// optional<scalar>

	@Test
	void whenReturnOptionalScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<String> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(false, d.isId());
		assertEquals(false, d.isNotNull());
		assertEquals(Optional.class, d.getOptionalDefinition().getContainer());

		assertEquals(null, d.getListType());
		assertEquals(false, d.isListNotNull());
		assertEquals(null, d.getOptionalListDefinition());

	}

	@Test
	void whenReturnOptionalIdScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<@GraphQLId String> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(true, d.isId());
		assertEquals(false, d.isNotNull());
		assertEquals(Optional.class, d.getOptionalDefinition().getContainer());

		assertEquals(null, d.getListType());
		assertEquals(false, d.isListNotNull());
		assertEquals(null, d.getOptionalListDefinition());

	}

	@Test
	void whenReturnOptionalNotNullIdScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<@GraphQLNotNull @GraphQLId String> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(true, d.isId());
		assertEquals(true, d.isNotNull());
		assertEquals(Optional.class, d.getOptionalDefinition().getContainer());

		assertEquals(null, d.getListType());
		assertEquals(false, d.isListNotNull());
		assertEquals(null, d.getOptionalListDefinition());

	}

	// list<?>

	@Test
	void whenReturnListScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public List<String> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(false, d.isId());
		assertEquals(false, d.isNotNull());
		assertEquals(null, d.getOptionalDefinition());

		assertEquals(List.class, d.getListType());
		assertEquals(false, d.isListNotNull());
		assertEquals(null, d.getOptionalListDefinition());

	}

	@Test
	void whenReturnListIdScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public List<@GraphQLId String> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(true, d.isId());
		assertEquals(false, d.isNotNull());
		assertEquals(null, d.getOptionalDefinition());

		assertEquals(List.class, d.getListType());
		assertEquals(false, d.isListNotNull());
		assertEquals(null, d.getOptionalListDefinition());

	}

	@Test
	void whenReturnListNotNullIdScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public List<@GraphQLNotNull @GraphQLId String> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(true, d.isId());
		assertEquals(true, d.isNotNull());
		assertEquals(null, d.getOptionalDefinition());

		assertEquals(List.class, d.getListType());
		assertEquals(false, d.isListNotNull());
		assertEquals(null, d.getOptionalListDefinition());

	}

	//

	@Test
	void whenReturnListOptionalScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public List<Optional<String>> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(false, d.isId());
		assertEquals(false, d.isNotNull());
		assertEquals(Optional.class, d.getOptionalDefinition().getContainer());

		assertEquals(List.class, d.getListType());
		assertEquals(false, d.isListNotNull());
		assertEquals(null, d.getOptionalListDefinition());

	}

	@Test
	void whenReturnListOptionalIdScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public List<Optional<@GraphQLId String>> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(true, d.isId());
		assertEquals(false, d.isNotNull());
		assertEquals(Optional.class, d.getOptionalDefinition().getContainer());

		assertEquals(List.class, d.getListType());
		assertEquals(false, d.isListNotNull());
		assertEquals(null, d.getOptionalListDefinition());

	}

	@Test
	void whenReturnListOptionalNotNullIdScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public List<Optional<@GraphQLNotNull @GraphQLId String>> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(true, d.isId());
		assertEquals(true, d.isNotNull());
		assertEquals(Optional.class, d.getOptionalDefinition().getContainer());

		assertEquals(List.class, d.getListType());
		assertEquals(false, d.isListNotNull());
		assertEquals(null, d.getOptionalListDefinition());

	}

	// notnull list<?>>

	@Test
	void whenReturnNotNullListScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public @GraphQLNotNull List<String> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(false, d.isId());
		assertEquals(false, d.isNotNull());
		assertEquals(null, d.getOptionalDefinition());

		assertEquals(List.class, d.getListType());
		assertEquals(true, d.isListNotNull());
		assertEquals(null, d.getOptionalListDefinition());

	}

	@Test
	void whenReturnNotNullListIdScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public @GraphQLNotNull List<@GraphQLId String> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(true, d.isId());
		assertEquals(false, d.isNotNull());
		assertEquals(null, d.getOptionalDefinition());

		assertEquals(List.class, d.getListType());
		assertEquals(true, d.isListNotNull());
		assertEquals(null, d.getOptionalListDefinition());

	}

	@Test
	void whenReturnNotNullListNotNullIdScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public @GraphQLNotNull List<@GraphQLNotNull @GraphQLId String> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(true, d.isId());
		assertEquals(true, d.isNotNull());
		assertEquals(null, d.getOptionalDefinition());

		assertEquals(List.class, d.getListType());
		assertEquals(true, d.isListNotNull());
		assertEquals(null, d.getOptionalListDefinition());

	}

	//

	@Test
	void whenReturnNotNullListOptionalScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public @GraphQLNotNull List<Optional<String>> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(false, d.isId());
		assertEquals(false, d.isNotNull());
		assertEquals(Optional.class, d.getOptionalDefinition().getContainer());

		assertEquals(List.class, d.getListType());
		assertEquals(true, d.isListNotNull());
		assertEquals(null, d.getOptionalListDefinition());

	}

	@Test
	void whenReturnNotNullListOptionalIdScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public @GraphQLNotNull List<Optional<@GraphQLId String>> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(true, d.isId());
		assertEquals(false, d.isNotNull());
		assertEquals(Optional.class, d.getOptionalDefinition().getContainer());

		assertEquals(List.class, d.getListType());
		assertEquals(true, d.isListNotNull());
		assertEquals(null, d.getOptionalListDefinition());

	}

	@Test
	void whenReturnNotNullListOptionalNotNullIdScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public @GraphQLNotNull List<Optional<@GraphQLNotNull @GraphQLId String>> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(true, d.isId());
		assertEquals(true, d.isNotNull());
		assertEquals(Optional.class, d.getOptionalDefinition().getContainer());

		assertEquals(List.class, d.getListType());
		assertEquals(true, d.isListNotNull());
		assertEquals(null, d.getOptionalListDefinition());

	}

	// optional list<?>

	@Test
	void whenReturnOptionalListScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<List<String>> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(false, d.isId());
		assertEquals(false, d.isNotNull());
		assertEquals(null, d.getOptionalDefinition());

		assertEquals(List.class, d.getListType());
		assertEquals(false, d.isListNotNull());
		assertEquals(Optional.class, d.getOptionalListDefinition().getContainer());

	}

	@Test
	void whenReturnOptionalListIdScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<List<@GraphQLId String>> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(true, d.isId());
		assertEquals(false, d.isNotNull());
		assertEquals(null, d.getOptionalDefinition());

		assertEquals(List.class, d.getListType());
		assertEquals(false, d.isListNotNull());
		assertEquals(Optional.class, d.getOptionalListDefinition().getContainer());

	}

	@Test
	void whenReturnOptionalListNotNullIdScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<List<@GraphQLNotNull @GraphQLId String>> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(true, d.isId());
		assertEquals(true, d.isNotNull());
		assertEquals(null, d.getOptionalDefinition());

		assertEquals(List.class, d.getListType());
		assertEquals(false, d.isListNotNull());
		assertEquals(Optional.class, d.getOptionalListDefinition().getContainer());

	}

	//

	@Test
	void whenReturnOptionalListOptionalScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<List<Optional<String>>> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(false, d.isId());
		assertEquals(false, d.isNotNull());
		assertEquals(Optional.class, d.getOptionalDefinition().getContainer());

		assertEquals(List.class, d.getListType());
		assertEquals(false, d.isListNotNull());
		assertEquals(Optional.class, d.getOptionalListDefinition().getContainer());

	}

	@Test
	void whenReturnOptionalListOptionalIdScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<List<Optional<@GraphQLId String>>> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(true, d.isId());
		assertEquals(false, d.isNotNull());
		assertEquals(Optional.class, d.getOptionalDefinition().getContainer());

		assertEquals(List.class, d.getListType());
		assertEquals(false, d.isListNotNull());
		assertEquals(Optional.class, d.getOptionalListDefinition().getContainer());

	}

	@Test
	void whenReturnOptionalListOptionalNotNullIdScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<List<Optional<@GraphQLNotNull @GraphQLId String>>> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(true, d.isId());
		assertEquals(true, d.isNotNull());
		assertEquals(Optional.class, d.getOptionalDefinition().getContainer());

		assertEquals(List.class, d.getListType());
		assertEquals(false, d.isListNotNull());
		assertEquals(Optional.class, d.getOptionalListDefinition().getContainer());

	}

	// optional notnull list<?>>

	@Test
	void whenReturnOptionalNotNullListScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<@GraphQLNotNull List<String>> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(false, d.isId());
		assertEquals(false, d.isNotNull());
		assertEquals(null, d.getOptionalDefinition());

		assertEquals(List.class, d.getListType());
		assertEquals(true, d.isListNotNull());
		assertEquals(Optional.class, d.getOptionalListDefinition().getContainer());

	}

	@Test
	void whenReturnOptionalNotNullListIdScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<@GraphQLNotNull List<@GraphQLId String>> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(true, d.isId());
		assertEquals(false, d.isNotNull());
		assertEquals(null, d.getOptionalDefinition());

		assertEquals(List.class, d.getListType());
		assertEquals(true, d.isListNotNull());
		assertEquals(Optional.class, d.getOptionalListDefinition().getContainer());

	}

	@Test
	void whenReturnOptionalNotNullListNotNullIdScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<@GraphQLNotNull List<@GraphQLNotNull @GraphQLId String>> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(true, d.isId());
		assertEquals(true, d.isNotNull());
		assertEquals(null, d.getOptionalDefinition());

		assertEquals(List.class, d.getListType());
		assertEquals(true, d.isListNotNull());
		assertEquals(Optional.class, d.getOptionalListDefinition().getContainer());

	}

	//

	@Test
	void whenReturnOptionalNotNullListOptionalScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<@GraphQLNotNull List<Optional<String>>> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(false, d.isId());
		assertEquals(false, d.isNotNull());
		assertEquals(Optional.class, d.getOptionalDefinition().getContainer());

		assertEquals(List.class, d.getListType());
		assertEquals(true, d.isListNotNull());
		assertEquals(Optional.class, d.getOptionalListDefinition().getContainer());

	}

	@Test
	void whenReturnOptionalNotNullListOptionalIdScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<@GraphQLNotNull List<Optional<@GraphQLId String>>> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(true, d.isId());
		assertEquals(false, d.isNotNull());
		assertEquals(Optional.class, d.getOptionalDefinition().getContainer());

		assertEquals(List.class, d.getListType());
		assertEquals(true, d.isListNotNull());
		assertEquals(Optional.class, d.getOptionalListDefinition().getContainer());

	}

	@Test
	void whenReturnOptionalNotNullListOptionalNotNullIdScalar_thenOk() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<@GraphQLNotNull List<Optional<@GraphQLNotNull @GraphQLId String>>> method() {
				return null;
			}
		};

		Method method = Arrays.stream(k.getClass().getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphWrappedTypeDefinition d = new GraphWrappedTypeDefinition(registry, method, method.getAnnotatedReturnType());

		assertEquals(String.class, d.getType());
		assertEquals(true, d.isId());
		assertEquals(true, d.isNotNull());
		assertEquals(Optional.class, d.getOptionalDefinition().getContainer());

		assertEquals(List.class, d.getListType());
		assertEquals(true, d.isListNotNull());
		assertEquals(Optional.class, d.getOptionalListDefinition().getContainer());

	}

}
