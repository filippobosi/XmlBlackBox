
package org.xmlblackbox.test.infrastructure.xml;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.xmlblackbox.test.infrastructure.exception.IncludeFileNotFound;
import org.xmlblackbox.test.infrastructure.exception.XmlValidationFault;
import org.xmlblackbox.xsd.TESTDocument;


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
	private List listaDbConnection= new Vector();
	private List listaPlugin = new Vector();
	
	private String nomeTest = null;
	

	public ReadXmlDocument(String navigationFile) throws Exception{
        InputStream iSValidate = this.getClass().getResourceAsStream(navigationFile);
        //navigationFile.mark(1000000);
        validateXml(iSValidate);
        //navigationFile.reset();
		InputStream iS = this.getClass().getResourceAsStream(navigationFile);
        loadFile(iS);
	}

    private void validateXml(InputStream iSValidate) throws XmlValidationFault{
//            InputStream iSValidate = this.getClass().getResourceAsStream(fileConfigTest);

            TESTDocument testDoc = null;
			try{
                    testDoc = TESTDocument.Factory.parse(iSValidate);
                    testDoc.save(new File("target/prova.xml"));
            }catch(Throwable e){
                e.printStackTrace();
                log.error("e "+e);
                log.error("THROWABLE ", e);
                throw new XmlValidationFault("");
            }
			ArrayList validationErrors = new ArrayList();
			XmlOptions voptions = new XmlOptions();
			voptions.setErrorListener(validationErrors);
			boolean valid = testDoc.validate(voptions);
			if(valid)
			{
				System.out.println("Its valid xml");
				log.info("Its valid xml");
			}
			else
			{
				log.info("Not a valid xml file");
				System.out.println("Not a valid xml file");
				Iterator itr = validationErrors.iterator();
				String errors = null;
				while(itr.hasNext())
				{
					String next = itr.next().toString();
					errors = errors +next;

					log.error(next);
					System.out.println(next);
				}
				throw new XmlValidationFault(errors);
			}

    }

	private void loadFile(InputStream navigationFile) throws Exception{
            SAXBuilder saxBuilder = new SAXBuilder();
            //saxBuilder.setValidation(false);

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
	
	    setNomeTest(element.getAttributeValue("name"));
    
	    log.info("[Parsing xml elements]");
	    while (elementList.hasNext()){
	    	Element element2 = (Element) elementList.next();
	    	log.info("[Element : " + element2 + " Name : " + element2.getName());
            log.info("element2 "+new XMLOutputter().outputString(element2));
        	try {
		    	if ("CHECK-DB".equals(element2.getName())) {
	            	DbCheck dbCheck = new DbCheck(element2);
		            listaDBCheck.add(dbCheck);
		            getListaCompleta().add(dbCheck);
		        } else if ("VALIDATE-XML".equals(element2.getName())) {
		        	XmlValidate xmlValidate= new XmlValidate(element2);
	            	listaHttpClient.add(xmlValidate);
		            getListaCompleta().add(xmlValidate);
		        } else if ("SELENIUM".equals(element2.getName())) {
		        	HTTPClient httpClient= new HTTPClient(element2, HTTPClient.SELENIUM);
	            	listaHttpClient.add(httpClient);
		            getListaCompleta().add(httpClient);
		        } else if ("HTTPTESTER".equals(element2.getName())) {
		        	HTTPClient httpClient= new HTTPClient(element2, HTTPClient.HTTPTESTER);
	            	listaHttpClient.add(httpClient);
		            getListaCompleta().add(httpClient);
		        } else if ("UPLOADER".equals(element2.getName())) {
		        	HTTPUploader httpUploader= new HTTPUploader(element2);
	            	listaHttpClient.add(httpUploader);
		            getListaCompleta().add(httpUploader);
		        } else if ("WEB-SERVICE-CLIENT".equals(element2.getName())) {
		        	WebServiceClient wsClient = new WebServiceClient(element2);
		        	listaWsClient.add(wsClient);
		        	getListaCompleta().add(wsClient);
		        } else if ("SET-VARIABLE".equals(element2.getName())) {
                    log.info("SET-VARIABLE element2 "+new XMLOutputter().outputString(element2));
		        	SetVariable setVariable = new SetVariable(element2);
		        	listaSetVariable.add(setVariable);
		        	getListaCompleta().add(setVariable);
		        } else if ("RUN-PLUGIN".equals(element2.getName())) {
		        	RunPlugin runPlugin = new RunPlugin(element2);
		        	listaPlugin.add(runPlugin);
		        	getListaCompleta().add(runPlugin);	        	
		        } else if ("EXECUTE-QUERY".equals(element2.getName())) {
		        	ExecuteQuery executeQuery = new ExecuteQuery(element2);
		        	listaExecuteQuery.add(executeQuery);
		        	getListaCompleta().add(executeQuery);
		        } else if ("XML-CONTENT".equals(element2.getName())) {
                    CheckInsertXmlContent checkXmlContent = new CheckInsertXmlContent(element2);
                    listaCheckXmlContent.add(checkXmlContent);
                    getListaCompleta().add(checkXmlContent);
		        } else if ("DB-CONNECTIONS".equals(element2.getName())) {
                    XmlDbConnections xmlDbConnections = new XmlDbConnections(element2);
                    listaDbConnection.add(xmlDbConnections);
                    getListaCompleta().add(xmlDbConnections);
                } else if ("INCLUDE-FILE".equals(element2.getName())) {
                    String filename = element2.getAttributeValue("filename");
                    log.debug("filename "+filename);
                    InputStream includeFile = this.getClass().getResourceAsStream(filename);
                    log.debug("includeFile "+includeFile);
                    if (includeFile==null){
                    	throw new IncludeFileNotFound("Include file not found "+filename);
                    }
                    this.loadFile(includeFile);
                }else{
            		log.error("Exception. The element " + element2.getName()+" does not exist");
                }
        	} catch (Exception e) {
        		log.fatal("[!] Errore durante il build dell'oggetto " + element2, e);
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
