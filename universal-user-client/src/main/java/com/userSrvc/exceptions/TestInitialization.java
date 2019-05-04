package com.userSrvc.exceptions;

import java.util.Arrays;

public class TestInitialization extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2990593985372019334L;
	private Class<?>[] dependencies;

	public TestInitialization(Class<?>...dependencies) {
		this.dependencies = dependencies;
	}

	@Override
	public String getMessage() {
		return "Error exists within the following classes: " + Arrays.toString(dependencies);
	}
}
