package com.nimbox.graphql.scalars;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.DateTimeException;

import com.nimbox.graphql.annotations.GraphQLScalar;

@GraphQLScalar(type = URL.class, name = "URL", description = "A url")
public class URLScalar extends StringBasedScalar<URL> {

	// constructors

	public URLScalar() {
		super(URL.class, URLScalar::format, URLScalar::parse);
	}

	// methods

	public static String format(URL url) throws DateTimeException {
		return url.toString();
	}

	public static URL parse(String string) throws MalformedURLException {
		return new URL(string);
	}

}
