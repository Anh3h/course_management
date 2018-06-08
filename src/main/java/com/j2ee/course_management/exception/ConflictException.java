package com.j2ee.course_management.exception;

import java.text.MessageFormat;

public class ConflictException extends RuntimeException {

	private ServiceError serviceError;

	public ConflictException( ServiceError serviceError ) {
		super(serviceError.getMessage());
		this.serviceError = serviceError;
	}

	public static ConflictException create( String message, Object... args ) {
		return new ConflictException(new ServiceError(409, MessageFormat.format(message, args)));
	}

}
