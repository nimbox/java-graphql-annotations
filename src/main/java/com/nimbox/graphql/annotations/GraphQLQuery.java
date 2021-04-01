package com.nimbox.graphql.annotations;

import static com.nimbox.graphql.utils.ReservedStringUtils.NULL;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface GraphQLQuery {

	String name();

	String description() default NULL;

	String deprecationReason() default NULL;

}