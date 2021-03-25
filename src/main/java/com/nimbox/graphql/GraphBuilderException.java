package com.nimbox.graphql;

public class GraphBuilderException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GraphBuilderException() {
	}

	public GraphBuilderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public GraphBuilderException(String message, Throwable cause) {
		super(message, cause);
	}

	public GraphBuilderException(String message) {
		super(message);
	}

	public GraphBuilderException(Throwable cause) {
		super(cause);
	}

}
