package com.nimbox.graphql.test;

import com.nimbox.graphql.MethodScanner;
import com.nimbox.graphql.TypeScanner;
import com.nimbox.graphql.registries.GraphRegistry;
import com.nimbox.graphql.test.domain.Episode;

public class ScannerRunner {

	public static void main(String[] args) {

		System.out.println(Episode.A_NEW_HOPE.getClass());

		GraphRegistry registry = new GraphRegistry();

		TypeScanner type = new TypeScanner(registry);
		type.withPackages( //
				ScannerRunner.class.getClassLoader(), //
				"com.nimbox" //
		);
		type.scan();

		MethodScanner code = new MethodScanner(registry);
		code.withPackages( //
				ScannerRunner.class.getClassLoader(), //
				"com.nimbox" //
		);
		code.scan();

	}

}
