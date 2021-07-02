package com.dio.santander.apimanagerpoints.services.exceptions;

public class ObjectAlreadyRegisteredException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ObjectAlreadyRegisteredException(String msg) {
		super(msg);
	}

	public ObjectAlreadyRegisteredException(String msg, Throwable clause) {
		super(msg, clause);
	}
}
