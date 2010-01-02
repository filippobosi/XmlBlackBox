package org.xmlblackbox.test.infrastructure;


import it.imolinfo.httptester.HttpTestCaseSimple;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.xmlblackbox.test.infrastructure.exception.TestException;
import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.util.Configurator;
import org.xmlblackbox.test.infrastructure.util.DBConnection;
import org.xmlblackbox.test.infrastructure.util.MemoryData;
import org.xmlblackbox.test.infrastructure.xml.CheckDatabase;
import org.xmlblackbox.test.infrastructure.xml.CheckInsertXmlContent;
import org.xmlblackbox.test.infrastructure.xml.ClientWeb;
import org.xmlblackbox.test.infrastructure.xml.DbConnection;
import org.xmlblackbox.test.infrastructure.xml.HTTPUploader;
import org.xmlblackbox.test.infrastructure.xml.Query;
import org.xmlblackbox.test.infrastructure.xml.ReadXmlDocument;
import org.xmlblackbox.test.infrastructure.xml.RunPlugin;
import org.xmlblackbox.test.infrastructure.xml.RunQuery;
import org.xmlblackbox.test.infrastructure.xml.Set;
import org.xmlblackbox.test.infrastructure.xml.SetVariable;
import org.xmlblackbox.test.infrastructure.xml.WaitTask;
import org.xmlblackbox.test.infrastructure.xml.WebServiceClient;
import org.xmlblackbox.test.infrastructure.xml.XmlCheckRow;
import org.xmlblackbox.test.infrastructure.xml.XmlDbConnections;
import org.xmlblackbox.test.infrastructure.xml.XmlElement;
import org.xmlblackbox.test.infrastructure.xml.XmlInsertRemoveNodeRow;
import org.xmlblackbox.test.infrastructure.xml.XmlInsertRow;
import org.xmlblackbox.test.infrastructure.xml.XmlRowInterface;
import org.xmlblackbox.test.infrastructure.xml.XmlValidate;

import com.thoughtworks.selenium.Selenium;
import org.apache.commons.lang.StringUtils;



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

public class FlowControl extends DatabaseTestCase {

    private final static Logger log = Logger.getLogger(FlowControl.class);

    private MemoryData memory = new MemoryData();

	private ClientWeb httpClient = null;
	private HTTPUploader httpUploader = null;
	private Selenium selenium = null;

	private HttpTestCaseSimple httpTestCase = null;

	//private IDatabaseConnection conn = null;
	private String nomeTestCase = null;

	public FlowControl(String msg) {
		super(msg);
	}

	public static Test suite() {
		log.debug("Log di FlowControl");
		TestSuite suite = new TestSuite(FlowControl.class);
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

			}
		} catch (Exception e) {
			log.debug("DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG ");
			log.error("[!] Reloading variables failed", e);
			throw e;
		}
	}


    public void execute(Class testClass, Properties prop)	throws TestException, Exception {

        log.info("execute testClass, prop, genericConnection");
    	String fileConfigTest = "." + testClass.getCanonicalName();
    	fileConfigTest = fileConfigTest.replaceAll("\\.", "/");
    	fileConfigTest = fileConfigTest + ".xml";

        execute(fileConfigTest, prop);

    }



    public void execute(String fileConfigTest, Properties prop) throws TestException, Exception {
        int step = 1;
        Object obj = null;
		try{

            log.info("execute fileConfigTest, prop, genericConnection");
			log.info("[ START TEST CASE : " + fileConfigTest.substring(0, fileConfigTest.indexOf(".")) + " ]");
            Properties xmlBlackboxProp = Configurator.getProperties("xmlBlackbox.properties");
            log.info("xmlBlackbox.properties " + xmlBlackboxProp);

			log.debug("[Starting Memory & File Properties]");
			memory.overrideRepository(Repository.FILE_PROPERTIES, xmlBlackboxProp);

            memory.addToRepository(Repository.FILE_PROPERTIES, prop);
			log.debug("[Starting Memory & File Properties][OK]");

			//conn = new DatabaseConnection(genericConn);

//            log.debug("System.getProperty(\"XBB_STEP\") "+System.getProperty("XBB_STEP"));
//            log.debug("System.getenv(\"XBB_STEP\") "+System.getenv("XBB_STEP"));
//            log.debug("System.getProperties() "+System.getProperties());
//            log.debug("System.getenv() "+System.getenv());
            String stepConfig = System.getenv("XBB_STEP");
            log.debug("[Reading XML TestCase]");

			log.debug("[Reading XML TestCase]");
			ReadXmlDocument readXmlDocument = null;
			try {
				readXmlDocument = new ReadXmlDocument(fileConfigTest);
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
			while (iterElement.hasNext()) {
	            log.info("[*][Starting execution TestCase : "+ nomeTestCase +" Step : "+step+"]");

				obj = iterElement.next();
				//memory.debugMemory();
				log.info("[Identify type node & Replacing variable xml]");
				replacingVariableXml(obj,memory);
				log.info("[Identify type node & Replacing variable xml][OK]");

				log.info("[Executing node]");
				executeNode((XmlElement) obj, step);
				log.info("[Executing node][OK]");
				step++;
                if (stepConfig!=null && stepConfig.equals(""+step)){
                    log.info("Exit configuration (-DXBB_STEP="+stepConfig+") a step "+stepConfig);
				    break;
                }
			}
		}catch(TestException e){
			if (memory!=null){
				memory.debugMemory();
			}
			if (obj!=null){
				log.error("TestException in step "+step+" ("+obj.getClass()+") of FlowControl.execute() ", e);
			}else{
				log.error("Exception in FlowControl.execute() ", e);
			}
			throw e;
		}catch(Exception e){
			if (memory!=null){
				memory.debugMemory();
			}
			if (obj!=null){
				log.error("Exception in in step "+step+" ("+obj.getClass()+") FlowControl.execute() ", e);
			}else{
				log.error("Exception in FlowControl.execute() ", e);
			}
			throw e;
		}finally{
            Hashtable hashObject = memory.getAllObject();
            Enumeration elements = hashObject.elements();
            while(elements.hasMoreElements()){
                Object objTmp = elements.nextElement();
				log.info("objTmp "+objTmp);
                if (objTmp instanceof Connection){
                    Connection conn = (Connection)objTmp;
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
            if (obj instanceof CheckDatabase) {
                CheckDatabase dbCheck = (CheckDatabase) obj;

                log.info("checkDB connection "+dbCheck.getConnection());
                Connection connDbCheck = memory.getConnectionByName(dbCheck.getConnection());

                dbCheck.checkDB(memory, new DatabaseConnection(connDbCheck), step);
            } else if (obj instanceof ClientWeb) {
                httpClient = (ClientWeb) obj;

                Properties webNavigationProp = memory.getOrCreateRepository(Repository.WEB_NAVIGATION);
                webNavigationProp.putAll(httpClient.getParameters());
                Properties parametersProperties = new Properties();
                log.info("httpClient.getParameters() "+httpClient.getParameters());
                parametersProperties.putAll(httpClient.getParameters());
                log.info("parametersProperties "+parametersProperties);
                
                memory.overrideRepository(Repository.PARAMETERS, parametersProperties);

                if (httpClient.getType().equals(ClientWeb.HTTPTESTER)){
                    httpTestCase = httpClient.executeHttpClient(httpClient, httpTestCase, memory);
                    webNavigationProp.putAll(httpTestCase.getResultVariables());
                }else if (httpClient.getType().equals(ClientWeb.SELENIUM)){
                    selenium = httpClient.executeSelenium(memory, selenium);
                }else{
                    throw new TestException("HTTP-CLIENT type "+httpClient.getType() +" not exist!!!");
                }
            } else if (obj instanceof HTTPUploader) {
                httpUploader = (HTTPUploader) obj;

                Properties webNavigationProp = memory.getOrCreateRepository(Repository.WEB_NAVIGATION);
                webNavigationProp.putAll(httpClient.getParameters());

                memory.getOrCreateRepository(Repository.WEB_NAVIGATION).putAll(memory.getRepository(Repository.FILE_PROPERTIES));
                memory.getOrCreateRepository(Repository.WEB_NAVIGATION).putAll(httpUploader.getParameters());

                httpUploader.uploadFile(memory, httpUploader);

            } else if (obj instanceof WebServiceClient) {
                WebServiceClient webServiceClient= (WebServiceClient) obj;
                //log.info("Eseguito il wsc "+webServiceClient.getNome());
                webServiceClient.executeWebServiceClient();
            } else if (obj instanceof WaitTask) {
            	WaitTask waitTask= (WaitTask) obj;
                log.info("Eseguito il waitTask "+waitTask.getNome());
            	waitTask.waitTask(memory);
            } else if (obj instanceof RunPlugin) {
                RunPlugin runPlugin= (RunPlugin) obj;
                log.info("Executed plugin: "+runPlugin.getTemplateClass());
                runPlugin.executePlugin(memory);
                log.info("memory: "+memory.getRepository(Repository.RUN_PLUGIN));
            } else if (obj instanceof XmlValidate) {
                XmlValidate xmlValidate= (XmlValidate) obj;
                xmlValidate.executeValidazioneXml();
            } else if (obj instanceof XmlDbConnections) {
                XmlDbConnections xmlDbConnections= (XmlDbConnections) obj;
                Iterator connectionList = xmlDbConnections.getDbConnectionList().iterator();
                while (connectionList.hasNext()) {
					DbConnection connection= (DbConnection) connectionList.next();
					Connection conn = DBConnection.getConnection(
							connection.getDriver(),
							connection.getDbUrl(),
							connection.getUsername(),
							connection.getPassword()
							);
                    log.info("connection.getName() "+connection.getName());
                    log.info("conn "+conn);
					memory.setConnection(connection.getName(), conn);

				}
            } else if (obj instanceof SetVariable) {
                log.debug("setVariable ");
                SetVariable setVariable= (SetVariable) obj;
                Iterator<Set> iter = setVariable.getSetList().iterator();
                log.debug("setVariable.getSetList().size() "+setVariable.getSetList().size());
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
                            SetVariable.setVariableFromDb(currentSet, memory);
                        }
                    }else{
                        throw new TestException("The type in SET tag ("+currentSet.getNome()+") is not correctly set");
                    }

                }
            } else if (obj instanceof RunQuery) {
                RunQuery executeQuery = (RunQuery) obj;
                Iterator<Query> iter = executeQuery.getQueryList().iterator();
                log.info("ExecuteQuery");
                log.info("executeQuery.getConnection() "+executeQuery.getConnection());

                while(iter.hasNext()){
                    Query query = iter.next();
                    IDatabaseConnection conn = new DatabaseConnection((Connection)memory.getConnectionByName(query.getConnection()));
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
                        XmlOptions xmlOptions = new XmlOptions();
                        xmlOptions.setSavePrettyPrint();

                        xobj.save(new File(checkXmlContent.getFileOutput()), xmlOptions);
                    }
                }catch(IOException e){
                    throw new TestException(e, "IOException");
                }
            }
        } catch (TestException e) {
            e.setErrorMessage("Node Failed "+obj.getName()+". "+e.toString());
            log.error(e.getClass().getSimpleName()+" [!] Execution Node Failed : "+obj.getName(),e);
            throw e;
        } catch (Exception e) {
            log.error(e.getClass().getSimpleName()+" [!] Execution Node Failed : "+obj.getName(), e);
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
