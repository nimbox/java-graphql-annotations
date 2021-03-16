package com.nimbox.graphql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.nimbox.graphql.utils.ReservedStrings;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.TYPE_USE })
public @interface GraphQLType {

	String name();

	String description() default ReservedStrings.UNDEFINED;

	String[] fieldOrder() default {};

}