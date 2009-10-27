package org.xmlblackbox.test.infrastructure;


import com.thoughtworks.selenium.Selenium;

import org.xmlblackbox.test.infrastructure.checks.CheckObject;
import org.xmlblackbox.test.infrastructure.checks.CheckObjectImpl;
import org.xmlblackbox.test.infrastructure.exception.TestException;
import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.util.DBConnection;
import org.xmlblackbox.test.infrastructure.util.MemoryData;
import org.xmlblackbox.test.infrastructure.xml.CheckInsertXmlContent;
import org.xmlblackbox.test.infrastructure.xml.DbCheck;
import org.xmlblackbox.test.infrastructure.xml.ExecuteQuery;
import org.xmlblackbox.test.infrastructure.xml.HTTPClient;
import org.xmlblackbox.test.infrastructure.xml.Query;
import org.xmlblackbox.test.infrastructure.xml.ReadXmlDocument;
import org.xmlblackbox.test.infrastructure.xml.RunFunction;
import org.xmlblackbox.test.infrastructure.xml.Set;
import org.xmlblackbox.test.infrastructure.xml.SetVariable;
import org.xmlblackbox.test.infrastructure.xml.WebServiceClient;
import org.xmlblackbox.test.infrastructure.xml.XmlCheckRow;
import org.xmlblackbox.test.infrastructure.xml.XmlElement;
import org.xmlblackbox.test.infrastructure.xml.XmlInsertRemoveNodeRow;
import org.xmlblackbox.test.infrastructure.xml.XmlInsertRow;
import org.xmlblackbox.test.infrastructure.xml.XmlRowInterface;
import org.xmlblackbox.test.infrastructure.xml.XmlValidate;
import it.imolinfo.httptester.HttpTestCaseSimple;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.xmlblackbox.test.infrastructure.util.Configurator;



/**
 * <p>
 * Title: 
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009	
 * </p>
 * <p>
 * Company: 
 * </p>
 *
 * @author Crea
 *
 */

public class MainTestCase extends DatabaseTestCase {

    private final static Logger log = Logger.getLogger(MainTestCase.class);

    private MemoryData memory = new MemoryData();
	
	private HTTPClient httpClient = null;
	private Selenium selenium = null;
	
	private HttpTestCaseSimple httpTestCase = null;

	private IDatabaseConnection conn = null;
	private String nomeTestCase = null;

	public MainTestCase(String msg) {
		super(msg);
	}

	public static Test suite() {
		log.debug("Log di MainTestCase");
		TestSuite suite = new TestSuite(MainTestCase.class);
		return suite;
	}

	/**
	 * Overwiring del metodo setUp(). Questo metodo viene chiamato da JUnit
	 * prima di eseguire il test.
	 */
	public void setUp() throws Exception {
		// richiamo il metodo setUp() definito nella helper class
	}

	/**
	 * Overwriting del metodo tearDown. Questo metodo viene chiamato da JUnit
	 * quando il test termina.
	 */
	public void tearDown() throws Exception {
		// log.info("Esecuzione tearDown");
	}

	public void loadPropsInDomain(Properties testProp) {
		memory.overrideRepository(Repository.FILE_PROPERTIES, testProp);
	}
	
	private void replacingVariableXml(Object obj,MemoryData memoryData) throws Exception{
		
		try {
			if(XmlElement.class.isAssignableFrom(obj.getClass()) ){
	
				XmlElement el=(XmlElement)obj;
				log.info("[-] Reloading variables tag : " + el.getXmlTagName() + " class : " + obj.getClass().getSimpleName());
				el.reload(memoryData);
				
				/*
				if (obj instanceof DbCheck) {
					log.info("++                                       ++");
					log.info("TROVATO DBCHECK "+((DbCheck) obj).getNome());
					log.info("++                                       ++");
					el.reload(memoryData);
				} else if (obj instanceof WebServiceClient) {
					log.info("++                                       ++");
					log.info("TROVATO Client WS "+((WebServiceClient) obj).getNome());
					log.info("++                                       ++");
					el.reload(memoryData);
				} else if (obj instanceof HTTPClient) {
					log.info("++                                       ++");
					log.info("TROVATO HTTP "+((HTTPClient) obj).getNome());
					log.info("++                                       ++");
					el.reload(memoryData);
				} else if (obj instanceof SetVariable) {
					log.info("++                                       ++");
					log.info("TROVATO SET-VARIABLE "+((SetVariable) obj).getNome());
					log.info("++                                       ++");
					el.reload(memoryData);
				} else if (obj instanceof ExecuteQuery) {
					log.info("++                                       ++");
					log.info("TROVATO EXECUTE-QUERY "+((ExecuteQuery) obj).getNome());
					log.info("++                                       ++");
					el.reload(memoryData);
				} else if (obj instanceof CheckInsertXmlContent) {
					log.info("++                                       ++");
                    log.info("TROVATO XML-CHECK/INSERT-CONTENT "+((CheckInsertXmlContent) obj).getNome());
					log.info("++                                       ++");
					el.reload(memoryData);
				} else if (obj instanceof RunFunction) {
					log.info("++                                       ++");
					log.info("TROVATO RUN FUNCTION "+((RunFunction) obj).getNome());
					log.info("++                                       ++");
					el.reload(memoryData);
				} else if (obj instanceof XmlValidate) {
					log.info("++                                       ++");
					log.info("TROVATO XML Validate "+((XmlValidate) obj).getNome());
					log.info("++                                       ++");
					el.reload(memoryData);
				}
				*/
			}
		} catch (Exception e) {
			log.fatal("[!] Reloading variables failed",e);
			throw e;
		}
	}

    
    public void execute(Class testClass, Properties prop, Connection genericConn)	throws TestException, Exception {

    	String fileConfigTest = "." + testClass.getCanonicalName();
    	fileConfigTest = fileConfigTest.replaceAll("\\.", "/");
    	fileConfigTest = fileConfigTest + ".xml";

        execute(fileConfigTest, prop, genericConn);

    }



	public void execute(String fileConfigTest, Properties prop, Connection genericConn) throws TestException, Exception {    	
		try{
			log.info("[ START TEST CASE : " + fileConfigTest.substring(0, fileConfigTest.indexOf(".")) + " ]");
            Properties xmlBlackboxProp = Configurator.getProperties("xmlBlackbox.properties");

			log.debug("[Starting Memory & File Properties]");
			memory.overrideRepository(Repository.FILE_PROPERTIES, xmlBlackboxProp);
            memory.addToRepository(Repository.FILE_PROPERTIES, prop);
			log.debug("[Starting Memory & File Properties][OK]");


			
//			log.debug("[DB Connection]");
//			// Connessione al DB...
//			DBConnection.setConfig(DBConnection.params.DRIVER, memory.get("db.driver@@"+Repository.FILE_PROPERTIES));
//			DBConnection.setConfig(DBConnection.params.URL, memory.get("db.url@"+Repository.FILE_PROPERTIES));
//			DBConnection.setConfig(DBConnection.params.USERNAME, memory.get("db.user@"+Repository.FILE_PROPERTIES));
//			DBConnection.setConfig(DBConnection.params.PASSWORD, memory.get("db.pw@"+Repository.FILE_PROPERTIES));
//			conn = DBConnection.getConnection();
//			log.debug("[DB Connection][OK]");
			
			conn = new DatabaseConnection(genericConn);
			
			InputStream iS = this.getClass().getResourceAsStream(fileConfigTest);

			log.debug("[Reading XML TestCase]");
			ReadXmlDocument readXmlDocument = null;
			try {
				readXmlDocument = new ReadXmlDocument(iS);
			} catch (DataSetException e1) {
				log.error("[!] Unable to read test file ",e1);
				throw new TestException( e1, "Unable to read test file "+fileConfigTest);
			} catch (IOException e1) {
				log.error("[!] Unable to read test file ",e1);
				throw new TestException( e1, "Unable to read test file  "+fileConfigTest);
			}catch (Exception e1) {
				log.error("[!] Unable to read test file ",e1);
				throw new TestException( e1, "Unable to read test file  "+fileConfigTest);
			}
			nomeTestCase = readXmlDocument.getNomeTest();
			log.info("[Preparing starting TestCase : " + nomeTestCase  + "]");
			log.debug("[Reading XML TestCase][OK]");

			Iterator iterElement = readXmlDocument.getListaCompleta().iterator();

            log.debug("[TestCase number of steps  : "+readXmlDocument.getListaCompleta().size()+"]");
            int step = 1;
			while (iterElement.hasNext()) {
	            log.info("[*][Starting execution TestCase : "+ nomeTestCase +" Step : "+step+"]");

				Object obj = iterElement.next();
				//memory.debugMemory();
				log.info("[Identify type node & Replacing variable xml]");
				replacingVariableXml(obj,memory);
				log.info("[Identify type node & Replacing variable xml][OK]");
				
				log.info("[Executing node]");
				executeNode((XmlElement) obj, step);
				log.info("[Executing node][OK]");	
				step++;
			}
		}catch(TestException e){
            memory.debugMemory();
			log.error("TestException in MainTestCase.execute() ", e);
			throw e;
		}catch(Exception e){
			memory.debugMemory();
			log.error("Exception in MainTestCase.execute() ", e);
			throw e;
		}finally{
			try {
				if (conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				log.error("Exception during database connection close", e );
			}
		}
	}
	
	private void waitTimeout(long timeout){
		try {
			Thread.sleep(1000*timeout);
		} catch (InterruptedException e) {
			log.error("InterruptedException nella sleep del Thread",e);
		}
	}
	
	private void executeNode(XmlElement obj, int step) throws Exception{
	
        try {
            if (obj instanceof DbCheck) {
                DbCheck dbCheck = (DbCheck) obj;
                dbCheck.checkDB(memory, conn, step);
            } else if (obj instanceof HTTPClient) {
                httpClient = (HTTPClient) obj;
                //httpClient.reload(memory);

                Properties webNavigationProp = memory.getOrCreateRepository(Repository.WEB_NAVIGATION);
                webNavigationProp.putAll(httpClient.getParameters());

                if (httpClient.getType().equals(HTTPClient.WEB_HTTPTESTER)){
                    httpTestCase = httpClient.executeHttpClient(conn, memory.getOrCreateRepository(Repository.FILE_PROPERTIES), httpClient, httpTestCase);
                    webNavigationProp.putAll(httpTestCase.getResultVariables());
                }else if (httpClient.getType().equals(HTTPClient.WEB_SELENIUM)){
                    selenium = httpClient.executeSelenium(conn, memory, selenium);
                }else if (httpClient.getType().equals(HTTPClient.UPLOAD_FILE)){

                    memory.getOrCreateRepository(Repository.WEB_NAVIGATION).putAll(memory.getRepository(Repository.FILE_PROPERTIES));
                    memory.getOrCreateRepository(Repository.WEB_NAVIGATION).putAll(httpClient.getParameters());

                    httpClient.uploadFile(memory, httpClient);
                }else{
                    throw new TestException("HTTP-CLIENT di tipo "+httpClient.getType() +" non esiste!!!");
                }

            } else if (obj instanceof WebServiceClient) {
                WebServiceClient webServiceClient= (WebServiceClient) obj;
                //log.info("Eseguito il wsc "+webServiceClient.getNome());
                webServiceClient.eseguiWebService(memory, step);
            } else if (obj instanceof RunFunction) {
                RunFunction runFunction= (RunFunction) obj;
                //log.info("Eseguito la funzione: "+runFunction.getTemplateClass());
                runFunction.executeFunction(memory);
            } else if (obj instanceof XmlValidate) {
                XmlValidate xmlValidate= (XmlValidate) obj;
                xmlValidate.executeValidazioneXml();
            } else if (obj instanceof SetVariable) {
                SetVariable setVariable= (SetVariable) obj;
                Iterator<Set> iter = setVariable.getSetList().iterator();
                while(iter.hasNext()){
                    Set currentSet = iter.next();

                    log.debug("setVariable.getRepositoryName() "+setVariable.getRepositoryName());
                    log.debug("currentSet.getNome() "+currentSet.getNome());
                    log.debug("currentSet.getValue() "+currentSet.getValue());
                    log.debug("currentSet.getType() " + currentSet.getType());

                    if (currentSet.getType()!=null){
                        if (currentSet.getType().equals(Set.VALUE_TYPE)){
                            memory.set(setVariable.getRepositoryName(),currentSet.getNome(),currentSet.getValue());
                        }else if(currentSet.getType().equals(Set.XML_TYPE)){
                            SetVariable.setVariableFromXml(currentSet, memory.getOrCreateRepository(setVariable.getRepositoryName()));
                        }else if(currentSet.getType().equals(Set.DB_TYPE)) {
                            SetVariable.setVariableFromDb(currentSet, memory.getOrCreateRepository(setVariable.getRepositoryName()), conn);
                        }
                    }else{
                        throw new TestException("The type in SET tag ("+currentSet.getNome()+") is not correctly set");
                    }

                }
            } else if (obj instanceof ExecuteQuery) {
                ExecuteQuery executeQuery = (ExecuteQuery) obj;
                Iterator<Query> iter = executeQuery.getQueryList().iterator();
                log.info("ExecuteQuery");
                while(iter.hasNext()){
                    Query query = iter.next();
                    log.info("Run query "+query.getQuery()+" about xml object "+query.getNome());
                    if (query.getType().equals(Query.UPDATE) || query.getType().equals(Query.INSERT)){
                        int result = conn.getConnection().createStatement().executeUpdate(query.getQuery());
                        log.info(query.getType()+"result "+result);
                    }else{
                        boolean result = conn.getConnection().createStatement().execute(query.getQuery());
                        log.info(query.getType()+"result "+result);
                    }
                }

            } else if (obj instanceof CheckInsertXmlContent) {
                CheckInsertXmlContent checkXmlContent = (CheckInsertXmlContent) obj;
                Iterator<XmlCheckRow> iter = checkXmlContent.getCheckXmlList().iterator();
                XmlObject xobj = checkXmlContent.getXmlObject();

                while(iter.hasNext()){
                    XmlRowInterface xmlRow = iter.next();
                    if (xmlRow.getType().equals(XmlCheckRow.TYPE)){
                        xobj = checkXmlContent.checkValueInXml(xobj, (XmlCheckRow)xmlRow, memory);
                    }else if (xmlRow.getType().equals(XmlInsertRow.TYPE)){
                        xobj = checkXmlContent.insertValueInXml(xobj, (XmlInsertRow)xmlRow, memory);
                    }else if (xmlRow.getType().equals(XmlInsertRemoveNodeRow.TYPE_INSERT)){
                        log.info("((XmlInsertRemoveNodeRow)xmlRow).getXPath() "+((XmlInsertRemoveNodeRow)xmlRow).getXPath());

                        xobj = checkXmlContent.insertNodeInXml(xobj, (XmlInsertRemoveNodeRow)xmlRow,
                                memory);
                        log.info("xobj.xmlText() "+xobj.xmlText());
                    }
                }

                try{
                    if (checkXmlContent.getFileOutput()!=null){
                        log.info("File output salvato "+checkXmlContent.getFileOutput());
                        xobj.save(new File(checkXmlContent.getFileOutput()));
                    }
                }catch(IOException e){
                    throw new TestException(e, "IOException");
                }
            }
        } catch (TestException e) {
            e.setErrorMessage("Node Failed "+obj.getName()+". "+e.toString());
            log.error("[!] Execution Node Failed : "+obj.getName(),e);
            throw e;
        } catch (Exception e) {
            log.error("[!] Execution Node Failed : "+obj.getName(),e);
            throw e;
        }
	}

	protected IDataSet getDataSet() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IDatabaseConnection getConnection() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}

/*
 * $Id:
 * $Log :
 */