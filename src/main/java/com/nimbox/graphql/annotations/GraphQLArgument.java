package com.nimbox.graphql.annotations;

import static com.nimbox.graphql.utils.ReservedStrings.UNDEFINED;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface GraphQLArgument {

	String name();

	String description() default UNDEFINED;

	String defaultValue() default UNDEFINED;

}