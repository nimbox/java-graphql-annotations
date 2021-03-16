package com.nimbox.graphql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.nimbox.graphql.utils.ReservedStrings;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GraphQLEnumValue {

	String name() default "";

	String description() default ReservedStrings.UNDEFINED;

	String deprecationReason() default ReservedStrings.UNDEFINED;

}