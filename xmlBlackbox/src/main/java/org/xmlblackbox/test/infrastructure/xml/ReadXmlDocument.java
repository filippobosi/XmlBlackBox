
package org.xmlblackbox.test.infrastructure.xml;

import org.xmlblackbox.test.infrastructure.replacement.ReplaceFromXML;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.dbunit.dataset.DataSetException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class ReadXmlDocument {
	
	private final static Logger log = Logger.getLogger(ReadXmlDocument.class);
	
	private List listaCompleta = new Vector();
	private List listaDBCheck = new Vector();
	private List listaHttpClient = new Vector();
	private List listaCheck = new Vector();
	private List listaWsClient = new Vector();
	private List listaSetVariable = new Vector();
	private List listaExecuteQuery = new Vector();
	private List listaCheckXmlContent = new Vector();
	
	private String nomeTest = null;
	

	public ReadXmlDocument(InputStream navigationFile) throws Exception{
		loadFile(navigationFile);
	}

	private void loadFile(InputStream navigationFile) throws Exception{
		SAXBuilder saxBuilder = new SAXBuilder();
		saxBuilder.setValidation(false);
		
		Document documentJdom = null;

        log.info("navigationFile "+navigationFile);
		try {
			
			documentJdom = saxBuilder.build(navigationFile);
	    } catch (JDOMException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
			e.printStackTrace();
		}
	    
	    Element element = documentJdom.getRootElement();	    
	    Iterator elementList = element.getChildren().iterator();
	
	    setNomeTest(element.getAttributeValue("nome"));
    
	    log.info("[Parsing xml elements]");
	    while (elementList.hasNext()){
	    	Element element2 = (Element) elementList.next();
	    	log.info("[Element : " + element2 + " Name : " + element2.getName());
        	try {
		    	if ("DATASETS_DBUNIT".equals(element2.getName())) {
	            	DbCheck dbCheck = new DbCheck(element2);
		            listaDBCheck.add(dbCheck);
		            getListaCompleta().add(dbCheck);
		        } else if ("VALIDATE-XML".equals(element2.getName())) {
		        	XmlValidate xmlValidate= new XmlValidate(element2);
	            	listaHttpClient.add(xmlValidate);
		            getListaCompleta().add(xmlValidate);
		        } else if ("HTTP-CLIENT".equals(element2.getName())) {
		        	HTTPClient httpClient= new HTTPClient(element2);
	            	listaHttpClient.add(httpClient);
		            getListaCompleta().add(httpClient);
		        } else if ("WEB-SERVICE-CLIENT".equals(element2.getName())) {
		        	WebServiceClient wsClient = new WebServiceClient(element2);
		        	listaWsClient.add(wsClient);
		        	getListaCompleta().add(wsClient);
		        } else if ("SET-VARIABLE".equals(element2.getName())) {
		        	SetVariable setVariable = new SetVariable(element2);
		        	listaSetVariable.add(setVariable);
		        	getListaCompleta().add(setVariable);
		        } else if ("RUN-FUNCTION".equals(element2.getName())) {
		        	RunFunction runFunction = new RunFunction(element2);
		        	listaSetVariable.add(runFunction);
		        	getListaCompleta().add(runFunction);	        	
		        } else if ("EXECUTE-QUERY".equals(element2.getName())) {
		        	ExecuteQuery executeQuery = new ExecuteQuery(element2);
		        	listaExecuteQuery.add(executeQuery);
		        	getListaCompleta().add(executeQuery);
		        } else if ("CHECK-XML-CONTENT".equals(element2.getName())) {
		        	CheckInsertXmlContent checkXmlContent = new CheckInsertXmlContent(element2);
		        	listaCheckXmlContent.add(checkXmlContent);
		        	getListaCompleta().add(checkXmlContent);
		        } else if ("INSERT-XML-CONTENT".equals(element2.getName())) {
		        	CheckInsertXmlContent checkXmlContent = new CheckInsertXmlContent(element2);
		        	listaCheckXmlContent.add(checkXmlContent);
		        	getListaCompleta().add(checkXmlContent);
		        } else if ("XML-CONTENT".equals(element2.getName())) {
                    CheckInsertXmlContent checkXmlContent = new CheckInsertXmlContent(element2);
                    listaCheckXmlContent.add(checkXmlContent);
                    getListaCompleta().add(checkXmlContent);
                } else if ("INCLUDE-FILE".equals(element2.getName())) {
                    String filename = element2.getAttributeValue("filename");
                    log.debug("filename "+filename);
                    InputStream includeFile = this.getClass().getResourceAsStream(filename);
                    log.debug("includeFile "+includeFile);
                    this.loadFile(includeFile);
                }
        	} catch (Exception e) {
        		log.fatal("[!] Errore durante il build dell'oggetto " + element2);
        		throw e;
			}
	   }
	   log.info("[Parsing xml elements]");
		
	}
	/*
	 * Per ora utilizzo un file per passare il target di DBUnit del file xml al sax parser 
	 * dovremmo utilizzare le Pipe
	 */
	

	public void setListaCompleta(List listaCompleta) {
		this.listaCompleta = listaCompleta;
	}

	public List getListaCompleta() {
		return listaCompleta;
	}

	public void setNomeTest(String nomeTest) {
		this.nomeTest = nomeTest;
	}

	public String getNomeTest() {
		return nomeTest;
	}

	public List getListaCheck() {
		return listaCheck;
	}

	public void setListaCheck(List listaCheck) {
		this.listaCheck = listaCheck;
	}

	public List getListaDBCheck() {
		return listaDBCheck;
	}

	public void setListaDBCheck(List listaDBCheck) {
		this.listaDBCheck = listaDBCheck;
	}

	public List getListaHttpClient() {
		return listaHttpClient;
	}

	public void setListaHttpClient(List listaHttpClient) {
		this.listaHttpClient = listaHttpClient;
	}

	public List getListaWsClient() {
		return listaWsClient;
	}

	public void setListaWsClient(List listaWsClient) {
		this.listaWsClient = listaWsClient;
	}

		
}

/*
 * $Id: $
 * $Log:$
 */