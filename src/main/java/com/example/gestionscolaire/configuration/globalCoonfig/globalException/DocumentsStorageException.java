package com.example.gestionscolaire.configuration.globalCoonfig.globalException;

public class DocumentsStorageException extends RuntimeException {

	public DocumentsStorageException(String message, Throwable cause) {
		super(message, cause);
	}

	public DocumentsStorageException(String message) {
		super(message);
	}

}
