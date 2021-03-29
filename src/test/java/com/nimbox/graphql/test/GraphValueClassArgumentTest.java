package com.nimbox.graphql.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nimbox.graphql.annotations.GraphQLArgument;
import com.nimbox.graphql.annotations.GraphQLId;
import com.nimbox.graphql.annotations.GraphQLInput;
import com.nimbox.graphql.annotations.GraphQLInputField;
import com.nimbox.graphql.definitions.GraphInputTypeDefinition;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.types.GraphOptionalDefinition;
import com.nimbox.util.Alternative;

class GraphValueClassArgumentTest {

	static final String ARGUMENT = "argument";

	GraphRegistry registry;

	@BeforeEach
	void beforeEach() {
		registry = new GraphRegistry();
		registry.withOptional(new GraphOptionalDefinition<>(Alternative.class, Alternative::undefined, Alternative::ofNullable));
	}

	//

	@Test
	void whenIdString_thenIdString() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public String method(@GraphQLArgument(name = ARGUMENT) @GraphQLId String argument) {
				return "Hello";
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		Parameter parameter = method.getParameters()[0];
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, parameter, parameter.getParameterizedType());

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
			public String method(@GraphQLArgument(name = ARGUMENT) @GraphQLId Integer argument) {
				return "Hello";
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		Parameter parameter = method.getParameters()[0];
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, parameter, parameter.getParameterizedType());

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
			public String method(@GraphQLArgument(name = ARGUMENT) String argument) {
				return "Hello";
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		Parameter parameter = method.getParameters()[0];
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, parameter, parameter.getParameterizedType());

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
			public void method(@GraphQLArgument(name = ARGUMENT) Optional<String> argument) {
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		Parameter parameter = method.getParameters()[0];
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, parameter, parameter.getParameterizedType());

		assertEquals(String.class, returnClass.getType());
		assertFalse(returnClass.isId());
		assertTrue(returnClass.isOptional());
		assertFalse(returnClass.isList());
		assertFalse(returnClass.isOptionalList());

	}

	@Test
	void whenAlternativeString_thenOptionalString() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public void method(@GraphQLArgument(name = ARGUMENT) Alternative<String> argument) {
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		Parameter parameter = method.getParameters()[0];
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, parameter, parameter.getParameterizedType());

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
			public String method(@GraphQLArgument(name = ARGUMENT) List<String> argument) {
				return "Hello";
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		Parameter parameter = method.getParameters()[0];
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, parameter, parameter.getParameterizedType());

		assertEquals(String.class, returnClass.getType());
		assertFalse(returnClass.isId());
		assertFalse(returnClass.isOptional());
		assertTrue(returnClass.isList());
		assertFalse(returnClass.isOptionalList());

	}

	@Test
	void whenListOptionalString_thenListOptionalString() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public String method(@GraphQLArgument(name = ARGUMENT) List<Optional<String>> argument) {
				return "Hello";
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		Parameter parameter = method.getParameters()[0];
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, parameter, parameter.getParameterizedType());

		assertEquals(String.class, returnClass.getType());
		assertFalse(returnClass.isId());
		assertTrue(returnClass.isOptional());
		assertTrue(returnClass.isList());
		assertFalse(returnClass.isOptionalList());

	}

	@Test
	void whenListAlternativeString_thenListAlternativeString() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public String method(@GraphQLArgument(name = ARGUMENT) List<Alternative<String>> argument) {
				return "Hello";
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		Parameter parameter = method.getParameters()[0];
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, parameter, parameter.getParameterizedType());

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
			public String method(@GraphQLArgument(name = ARGUMENT) Optional<List<String>> argument) {
				return "Hello";
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		Parameter parameter = method.getParameters()[0];
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, parameter, parameter.getParameterizedType());

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
			public String method(@GraphQLArgument(name = ARGUMENT) Optional<List<Optional<String>>> argument) {
				return "Hello";
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		Parameter parameter = method.getParameters()[0];
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, parameter, parameter.getParameterizedType());

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
			public String method(@GraphQLArgument(name = ARGUMENT) Optional<List<Alternative<String>>> argument) {
				return "Hello";
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		Parameter parameter = method.getParameters()[0];
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, parameter, parameter.getParameterizedType());

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
			public String method(@GraphQLArgument(name = ARGUMENT) Alternative<List<String>> argument) {
				return "Hello";
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		Parameter parameter = method.getParameters()[0];
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, parameter, parameter.getParameterizedType());

		assertEquals(String.class, returnClass.getType());
		assertFalse(returnClass.isId());
		assertFalse(returnClass.isOptional());
		assertTrue(returnClass.isList());
		assertTrue(returnClass.isOptionalList());

	}

	@Test
	void whenAlternativeListOptionalString_thenAlternativeListOptionalString() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public String method(@GraphQLArgument(name = ARGUMENT) Alternative<List<Optional<String>>> argument) {
				return "Hello";
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		Parameter parameter = method.getParameters()[0];
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, parameter, parameter.getParameterizedType());

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
			public String method(@GraphQLArgument(name = ARGUMENT) Alternative<List<Alternative<String>>> argument) {
				return "Hello";
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		Parameter parameter = method.getParameters()[0];
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, parameter, parameter.getParameterizedType());

		assertEquals(String.class, returnClass.getType());
		assertFalse(returnClass.isId());
		assertTrue(returnClass.isOptional());
		assertTrue(returnClass.isList());
		assertTrue(returnClass.isOptionalList());

	}

	// inputs

	@GraphQLInput(name = "SomeInput")
	public interface Input {

		@GraphQLInputField(name = "name")
		String getName();

	}

	@Test
	void whenInput_thenInput() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public void method(@GraphQLArgument(name = ARGUMENT) Input argument) {
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		Parameter parameter = method.getParameters()[0];
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, parameter, parameter.getParameterizedType());

		assertEquals(Input.class, returnClass.getType());
		assertFalse(returnClass.isId());
		assertFalse(returnClass.isOptional());
		assertFalse(returnClass.isList());
		assertFalse(returnClass.isOptionalList());

	}

	@Test
	void whenParameterOptionalInput_thenOptionalInput() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public void method(@GraphQLArgument(name = ARGUMENT) Optional<Input> argument) {
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		Parameter parameter = method.getParameters()[0];
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, parameter, parameter.getParameterizedType());

		assertEquals(Input.class, returnClass.getType());
		assertFalse(returnClass.isId());
		assertTrue(returnClass.isOptional());
		assertFalse(returnClass.isList());
		assertFalse(returnClass.isOptionalList());

	}

	@Test
	void whenAlternativeInput_thenAlternativeInput() {

		Object k = new Object() {
			@SuppressWarnings("unused")
			public void method(@GraphQLArgument(name = ARGUMENT) Alternative<Input> argument) {
			}
		};

		Class<?> klass = k.getClass();
		Method method = Arrays.stream(klass.getMethods()).filter(m -> m.getName().equals("method")).findFirst().get();
		Parameter parameter = method.getParameters()[0];
		GraphInputTypeDefinition returnClass = new GraphInputTypeDefinition(registry, parameter, parameter.getParameterizedType());

		assertEquals(Input.class, returnClass.getType());
		assertFalse(returnClass.isId());
		assertTrue(returnClass.isOptional());
		assertFalse(returnClass.isList());
		assertFalse(returnClass.isOptionalList());

	}

}
