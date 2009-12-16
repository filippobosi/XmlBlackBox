package org.xmlblackbox.test.infrastructure.exception;

public class XmlValidationFault extends Exception {

	public XmlValidationFault(String errorMessage) {
		super("[!] Validazione XML Fallita : " + errorMessage);
	}

}
