package org.xmlblackbox.test.infrastructure.exception;

public class RepositoryNotFound extends Exception
{

	public RepositoryNotFound(String repositoryName) {
		super("Repository non trovato : " + repositoryName);
	}

}
