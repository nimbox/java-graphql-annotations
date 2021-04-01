package com.nimbox.graphql.utils;

public class ReservedStringUtils {

	public static final String UNDEFINED = "\u200B\u202F\uFEFF<undefined>\u200B\u202F\uFEFF";
	public static final String NULL = "\u200B\u202F\uFEFF<null>\u200B\u202F\uFEFF";

	public static boolean isDefined(String s) {
		return s != null && !UNDEFINED.equals(s) && !NULL.equals(s);
	}

	public static String translate(String s) {
		return isDefined(s) ? s : null;
	}

}
