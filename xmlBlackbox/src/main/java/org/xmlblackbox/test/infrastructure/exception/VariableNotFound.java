package org.xmlblackbox.test.infrastructure.exception;

import java.util.Hashtable;

import org.apache.log4j.Logger;

public class VariableNotFound extends TestException {
	
	public VariableNotFound(String key,Hashtable repository) {
		super();
		log.error("---------------------------------------------------------");
		log.error("[!] Variable not found : " + key);
		log.error("[!] Repository contains : " + repository);
		log.error("---------------------------------------------------------");
	}
	
}
