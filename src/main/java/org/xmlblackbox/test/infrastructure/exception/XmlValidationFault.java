package org.xmlblackbox.test.infrastructure.exception;

public class XmlValidationFault extends TestException {

	public XmlValidationFault(String errorMessage) {
		super("[!] Validazione XML Fallita : " + errorMessage);
	}

}
