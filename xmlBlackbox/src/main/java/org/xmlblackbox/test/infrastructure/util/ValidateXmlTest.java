package org.xmlblackbox.test.infrastructure.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.xmlblackbox.test.infrastructure.exception.XmlValidationFault;
import org.xmlblackbox.xsd.TESTDocument;


public class ValidateXmlTest {

	private final static Logger log = Logger.getLogger(ValidateXmlTest.class);
	
	private InputStream xmlInputStream = null;
	private String message = "";
	
	
	public ValidateXmlTest(InputStream iS) {
		this.xmlInputStream = iS;
	}
	
	
	public void validate() throws XmlValidationFault{
		boolean ret = false;
		
		try {
		
			TESTDocument testDocument = TESTDocument.Factory.parse(xmlInputStream);
			
			if (!testDocument.validate()){
				throw new XmlValidationFault("Validation failed");
			}
			
		} catch (IOException e) {
			log.fatal("Validation exception ",e);
			throw new XmlValidationFault(e.getMessage());
		} catch (XmlException e) {
			log.fatal("Validation exception ",e);
			throw new XmlValidationFault(e.getMessage());
		}catch (Exception e) {
			log.fatal("Validation exception ",e);
			throw new XmlValidationFault(e.getMessage());
		}
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}



	
}
