package org.xmlblackbox.test.infrastructure.util;

import org.xmlblackbox.test.infrastructure.exception.XmlValidationFault;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class ValidateXML {

	private final static Logger log = Logger.getLogger(ValidateXML.class);
	
	private File xml = null;
	private File xsd = null;
	private String message = "";
	
	
	public ValidateXML(String xml,String xsd) {
		this.xml = new File(xml);
		this.xsd = new File(xsd);
	}

	private Document generaDocument(File xml) {
	Document d = null;

	try {
		//Creazione documento da stringa xml
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
		// RISOLVE BUG JDK1.6 - http://weblogs.java.net/blog/spericas/archive/2007/03/xml_schema_vali.html
		factory.setNamespaceAware(true); 
		DocumentBuilder parser = factory.newDocumentBuilder();
		d = parser.parse(xml);
		} catch (ParserConfigurationException e) {
			log.error("Errore durante il parsing del file xml",e);
		} catch (SAXException e) {
			log.error("Errore durante il parsing del file xml",e);
		} catch (IOException e) {
			log.fatal("Errore durante il parsing del file xml",e);
		}
		finally {
			return d;
		}
	}
	
	/**
	* 
	* @return
	*/
	private Validator generaValidatore(File xsd) {
	Validator v = null;

	try {
	//Creazione dello schema di validazione dell'xml
	SchemaFactory schemaFactory = SchemaFactory.newInstance( "http://www.w3.org/2001/XMLSchema" );
	//SchemaFactory schemaFactory = SchemaFactory.newInstance( XMLConstants.XMLNS_ATTRIBUTE_NS_URI );
	Schema schemaXSD = schemaFactory.newSchema( xsd );
	v = schemaXSD.newValidator();
	} catch (SAXException e) {
		log.fatal("Errore durante la generazione del validatore XSD",e);
	}
	finally {
		return v;
	}
	}
	
	public void validate() throws XmlValidationFault{
		boolean ret = false;
		
		try {
		Document d = generaDocument(xml);
		Validator v = generaValidatore(xsd);

		//Validazione dell'xml
		v.setFeature("http://xml.org/sax/features/validation",true);
		v.setFeature("http://apache.org/xml/features/validation/schema",true);		
		v.validate( new DOMSource( d ) );
		ret = true;
		}
		catch (SAXException e) { 
			e.printStackTrace();
			log.error("Validazione fallita : ", e);
			log.warn("Validazione fallita : " + e.getMessage());
			throw new XmlValidationFault(e.getMessage());
		} catch (IOException e) {
			log.fatal("Impossibile eseguire la validazione",e);
		}
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}



	
}
