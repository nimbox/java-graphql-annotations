package com.nimbox.graphql.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nimbox.graphql.GraphBuilderException;
import com.nimbox.graphql.annotations.GraphQLId;
import com.nimbox.graphql.definitions.GraphInputTypeDefinition;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.test.TypeObjectTest.ExecutionContext;
import com.nimbox.graphql.types.GraphOptionalDefinition;
import com.nimbox.util.Alternative;

class GraphValueClassReturnTest {

	GraphRegistry registry;

	@BeforeEach
	void beforeEach() {
		registry = new GraphRegistry(ExecutionContext.class);
		registry.withOptional(new GraphOptionalDefinition<>(Alternative.class, Alternative::undefined, Alternative::ofNullable));
	}

	//

	@Test
	void whenVoid_thenException() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public void method() {
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();

		assertThrows(GraphBuilderException.class, () -> {
			new GraphInputTypeDefinition(registry, method, method.getGenericReturnType());
		});

	}

	//

	@Test
	void whenIdString_thenIdString() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public @GraphQLId String method() {
				return "something";
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, method, method.getGenericReturnType());

		assertEquals(String.class, returnClass.getType());
		assertTrue(returnClass.isId());
		assertFalse(returnClass.isOptional());
		assertFalse(returnClass.isList());
		assertFalse(returnClass.isOptionalList());

	}

	@Test
	void whenInteger_thenInteger() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public @GraphQLId Integer method() {
				return 0;
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, method, method.getGenericReturnType());

		assertEquals(Integer.class, returnClass.getType());
		assertTrue(returnClass.isId());
		assertFalse(returnClass.isOptional());
		assertFalse(returnClass.isList());
		assertFalse(returnClass.isOptionalList());

	}

	//

	@Test
	void whenString_thenString() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public String method() {
				return "something";
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, method, method.getGenericReturnType());

		assertEquals(String.class, returnClass.getType());
		assertFalse(returnClass.isId());
		assertFalse(returnClass.isOptional());
		assertFalse(returnClass.isList());
		assertFalse(returnClass.isOptionalList());

	}

	@Test
	void whenOptionalString_thenOptionalString() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<String> method() {
				return Optional.of("something");
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, method, method.getGenericReturnType());

		assertEquals(String.class, returnClass.getType());
		assertFalse(returnClass.isId());
		assertTrue(returnClass.isOptional());
		assertFalse(returnClass.isList());
		assertFalse(returnClass.isOptionalList());

	}

	@Test
	void whenAlternativeString_thenAlternativeString() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Alternative<String> method() {
				return Alternative.of("something");
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, method, method.getGenericReturnType());

		assertEquals(String.class, returnClass.getType());
		assertFalse(returnClass.isId());
		assertTrue(returnClass.isOptional());
		assertFalse(returnClass.isList());
		assertFalse(returnClass.isOptionalList());

	}

	//

	@Test
	void whenListString_thenListString() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public List<String> method() {
				return Arrays.asList("something");
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, method, method.getGenericReturnType());

		assertEquals(String.class, returnClass.getType());
		assertFalse(returnClass.isId());
		assertFalse(returnClass.isOptional());
		assertTrue(returnClass.isList());
		assertFalse(returnClass.isOptionalList());

	}

	@Test
	void whenListOptionalString_thenlListOptionalString() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public List<Optional<String>> method() {
				return Arrays.asList(Optional.of("something"));
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, method, method.getGenericReturnType());

		assertEquals(String.class, returnClass.getType());
		assertFalse(returnClass.isId());
		assertTrue(returnClass.isOptional());
		assertTrue(returnClass.isList());
		assertFalse(returnClass.isOptionalList());

	}

	@Test
	void whenListAlternativeString_thenlListAlternativeString() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public List<Alternative<String>> method() {
				return Arrays.asList(Alternative.of("something"));
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, method, method.getGenericReturnType());

		assertEquals(String.class, returnClass.getType());
		assertFalse(returnClass.isId());
		assertTrue(returnClass.isOptional());
		assertTrue(returnClass.isList());
		assertFalse(returnClass.isOptionalList());

	}

	//

	@Test
	void whenOptionalListString_thenOptionalListString() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<List<String>> method() {
				return Optional.of(Arrays.asList("something"));
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, method, method.getGenericReturnType());

		assertEquals(String.class, returnClass.getType());
		assertFalse(returnClass.isId());
		assertFalse(returnClass.isOptional());
		assertTrue(returnClass.isList());
		assertTrue(returnClass.isOptionalList());

	}

	@Test
	void whenOptionalListOptionalString_thenlOptionalListOptionalString() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<List<Optional<String>>> method() {
				return Optional.of(Arrays.asList(Optional.of("something")));
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, method, method.getGenericReturnType());

		assertEquals(String.class, returnClass.getType());
		assertFalse(returnClass.isId());
		assertTrue(returnClass.isOptional());
		assertTrue(returnClass.isList());
		assertTrue(returnClass.isOptionalList());

	}

	@Test
	void whenOptionalListAlternativeString_thenlOptionalListAlternativeString() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<List<Alternative<String>>> method() {
				return Optional.of(Arrays.asList(Alternative.of("something")));
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, method, method.getGenericReturnType());

		assertEquals(String.class, returnClass.getType());
		assertFalse(returnClass.isId());
		assertTrue(returnClass.isOptional());
		assertTrue(returnClass.isList());
		assertTrue(returnClass.isOptionalList());

	}

	//

	@Test
	void whenAlternativeListString_thenAlternativeListString() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Alternative<List<String>> method() {
				return Alternative.of(Arrays.asList("something"));
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, method, method.getGenericReturnType());

		assertEquals(String.class, returnClass.getType());
		assertFalse(returnClass.isId());
		assertFalse(returnClass.isOptional());
		assertTrue(returnClass.isList());
		assertTrue(returnClass.isOptionalList());

	}

	@Test
	void whenAlternativeListOptionalString_thenlAlternativeListOptionalString() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Alternative<List<Optional<String>>> method() {
				return Alternative.of(Arrays.asList(Optional.of("something")));
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, method, method.getGenericReturnType());

		assertEquals(String.class, returnClass.getType());
		assertFalse(returnClass.isId());
		assertTrue(returnClass.isOptional());
		assertTrue(returnClass.isList());
		assertTrue(returnClass.isOptionalList());

	}

	@Test
	void whenAlternativeListAlternativeString_thenAlternativeListAlternativeString() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Alternative<List<Alternative<String>>> method() {
				return Alternative.of(Arrays.asList(Alternative.of("something")));
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, method, method.getGenericReturnType());

		assertEquals(String.class, returnClass.getType());
		assertFalse(returnClass.isId());
		assertTrue(returnClass.isOptional());
		assertTrue(returnClass.isList());
		assertTrue(returnClass.isOptionalList());

	}

	//

	@Test
	void whenOptionalListOptionalString_thenOptionalListOptionalString() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<List<Optional<String>>> method() {
				return Optional.of(Arrays.asList(Optional.of("something")));
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();

		GraphInputTypeDefinition returnType = new GraphInputTypeDefinition(registry, method, method.getGenericReturnType());
		assertFalse(returnType.isId());
		assertEquals(String.class, returnType.getType());
		assertTrue(returnType.isOptional());
		assertTrue(returnType.isList());
		assertTrue(returnType.isOptionalList());

	}

	//
	//
	//

	@Test
	void whenListNonOptional_thenException() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public List<Map<String, String>> method() {
				return Collections.emptyList();
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();

		assertThrows(GraphBuilderException.class, () -> {
			new GraphInputTypeDefinition(registry, method, method.getGenericReturnType());
		});

	}

	@Test
	void whenOptionalNonList_thenException() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<Map<String, String>> method() {
				return Optional.of(Collections.emptyMap());
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();

		assertThrows(GraphBuilderException.class, () -> {
			new GraphInputTypeDefinition(registry, method, method.getGenericReturnType());
		});

	}

	@Test
	void whenOptionalListNonOptional_thenException() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<List<Map<String, String>>> method() {
				return Optional.of(Collections.emptyList());
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();

		assertThrows(GraphBuilderException.class, () -> {
			new GraphInputTypeDefinition(registry, method, method.getGenericReturnType());
		});

	}

	@Test
	void whenOptionalListOptionalNon_thenException() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public Optional<List<Optional<Map<String, String>>>> method() {
				return Optional.of(Collections.emptyList());
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();

		assertThrows(GraphBuilderException.class, () -> {
			new GraphInputTypeDefinition(registry, method, method.getGenericReturnType());
		});

	}

}
