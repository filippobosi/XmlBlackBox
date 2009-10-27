package org.xmlblackbox.test.infrastructure.exception;

public class RepositoryNotFound extends TestException {

	public RepositoryNotFound(String repositoryName) {
		super("Repository non trovato : " + repositoryName);
	}

}
