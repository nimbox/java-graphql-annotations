package com.nimbox.graphql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.nimbox.graphql.utils.ReservedStrings;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface GraphQLQuery {

	String name();

	String description() default ReservedStrings.NULL;

	String deprecationReason() default ReservedStrings.NULL;

}