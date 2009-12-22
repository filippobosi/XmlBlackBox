
package org.xmlblackbox.test.infrastructure.xml;

import it.imolinfo.httptester.HttpTestCaseSimple;
import it.imolinfo.httptester.WebNavigationSession;
import it.imolinfo.httptester.exception.SystemException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.jdom.Element;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;


import org.xmlblackbox.test.infrastructure.exception.TestException;
import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.interfaces.SeleniumNavigation;
import org.xmlblackbox.test.infrastructure.util.MemoryData;

import com.tapsterrock.jiffie.JiffieException;
import com.thoughtworks.selenium.Selenium;
import java.sql.Connection;
import org.dbunit.database.DatabaseConnection;
import org.jdom.Namespace;
/**
 * Es.
 *  Login fatta con Selenium all'indirizzo WEB_URL@testProp trovato nel
 *  file di properties (testProp) con utente e password che poi vengono
 *  utilizzati nella navigazione selenium
 * <br>
 *  &lt; SELENIUM nome="Login con SELENIUM"&gt; <br>
 *		&lt; SELENIUM-NAVIGATION&gt;it.example.test.selenium.GenericLogin&lt; /SELENIUM-NAVIGATION&gt; <br>
 *		&lt; PARAMETERS&gt; <br>
 *			&lt; PARAMETER name="WEB_URL" value="${WEB_URL@testProp}" /&gt; <br>
 *			&lt; PARAMETER name="username" value="${USERNAME_COMUNE@testProp}"/&gt; <br>
 *			&lt; PARAMETER name="password" value="${PASSWORD_COMUNE@testProp}"/&gt; <br>
 *		&lt; /PARAMETERS&gt; <br>
 *	&lt; /SELENIUM&gt; <br><br>
 *
 *  Creazione della domanda economica
 * <br>
 *  &lt; SELENIUM nome="EsportaAnagrafica001 Crea domanda economica PRESA IN CARICO"&gt; <br>
 *		&lt; SELENIUM-NAVIGATION&gt;it.example.test.selenium.CreaDomandaEconomica&lt; /SELENIUM-NAVIGATION&gt; <br>
 *		&lt; PARAMETERS&gt; <br>
 *			&lt; PARAMETER name="click_button_finale" value="_finish_presenta"/&gt; <br>
 *			&lt; PARAMETER name="potenza_impegnata" value="3"/&gt; <br>
 *          &lt; PARAMETER name="codice_provincia_comune" value="${CODICE_PROVINCIA_COMUNE@testProp}"/&gt; <br>
 *			&lt; PARAMETER name="codice_comune" value="${CODICE_COMUNE@testProp}"/&gt; <br>
 *		&lt; /PARAMETERS&gt; <br>
 *	&lt; /SELENIUM&gt; <br>
 *
 *
 * @author acrea
 */
public class ClientWeb extends Runnable  {

	private final static Logger logger = Logger.getLogger(ClientWeb.class);

	public static String HTTPTESTER = "HTTPTESTER";
	public static String SELENIUM = "SELENIUM";
	public static String UPLOAD_FILE = "UPLOAD_FILE";

	private Map<String, String> parameters = new HashMap<String, String>();
	private String outputPrefix = null;
	private String fileNavigation= null;
	private String application = null;
	private String urlUpload= null;

	private boolean startConversation=false;
	private boolean waitingTerminated=false;
	private String waitingQuery = null;
	private String waitingTimeout= "100";
	private String waitingConnection = null;
	private String type;

	int timeout = -1;

	public ClientWeb(Element el, String type) throws Exception {
		super(el);
        this.type = type;
        build(el);
	}

	public void build(Element clientElement) throws Exception {
		parameters = new HashMap<String, String>();

        Element parametersElement = clientElement.getChild("PARAMETERS", uriXsd);
    	if (parametersElement!=null) {
    		Iterator parametersList = parametersElement.getChildren("PARAMETER", uriXsd).iterator();
    		while (parametersList.hasNext()){
    			Element parameterElement = (Element) parametersList.next();
    			String pname = parameterElement.getAttributeValue("name");
    			String pvalue = parameterElement.getAttributeValue("value");

    			parameters.put(pname, pvalue);
    		}
    	}
		logger.debug("parameters "+parameters);

		ClientWeb httpClient=this;
		logger.debug("clientElement "+clientElement);
		logger.debug("clientElement.getAttributeValue(nome) "+clientElement.getAttributeValue("name"));
		httpClient.setNome(clientElement.getAttributeValue("name"));

		if(clientElement.getAttributeValue("start")!=null)
			httpClient.setStartConversation(true);

//    	Element timeout = clientElement.getChild("TIMEOUT");
//    	if (timeout!=null)
////    		httpClient.setTimeout(Integer.parseInt(timeout.getText()));

    	Element fileNavigationXml= clientElement.getChild("FILE-NAVIGATION", uriXsd);

    	if (fileNavigationXml!=null){
    		httpClient.setFileNavigation(fileNavigationXml.getText());
    	}

//    	Element fileInput= clientElement.getChild("FILE_INPUT");
//    	if (fileInput!=null){
//    		httpClient.setFileInput(fileInput.getText());
//    	}
//
//    	Element fileOutput= clientElement.getChild("FILE_OUTPUT");
//    	if (fileOutput!=null){
//    		httpClient.setFileOutput(fileOutput.getText());
//    	}
//
//    	Element application = clientElement.getChild("APPLICATION");
//    	if (application!=null){
//    		httpClient.setApplication(application.getAttributeValue("name"));
////    	}

//    	Element urlApload = clientElement.getChild("URL_UPLOAD");
//		logger.debug("urlApload "+urlApload);
//    	if (urlApload!=null){
//    		logger.debug("urlApload.getText() "+urlApload.getText());
//    		httpClient.setUrlUpload(urlApload.getText());
//    	}

    	Element type = clientElement.getChild("TYPE", uriXsd);
    	if (type!=null){
    		httpClient.setType(type.getAttributeValue("name"));
    	}

    	Element waitTerminated = clientElement.getChild("WAIT_TERMINATED", uriXsd);
    	logger.info("waitTerminated "+waitTerminated);
    	if (waitTerminated!=null){
    		httpClient.setWaitingTerminated(true);
    		httpClient.setWaitingQuery(waitTerminated.getAttributeValue("query"));

    		logger.info("waitTerminated.getAttributeValue(\"timeout\") "+waitTerminated.getAttributeValue("timeout"));
        	if (waitTerminated.getAttributeValue("timeout")!=null){
    			httpClient.setWaitingTimeout(waitTerminated.getAttributeValue("timeout"));
    		}
            httpClient.setWaitingConnection(waitTerminated.getAttributeValue("connection"));

    	}

//    	if (waitTerminated!=null){
//    		httpClient.setWaitingTerminated(true);
//    		httpClient.setWaitingTable(waitTerminated.getAttributeValue("table"));
//    		httpClient.setWaitingColumn(waitTerminated.getAttributeValue("column"));
//    		if (waitTerminated.getAttributeValue("timeout")!=null){
//    			httpClient.setWaitingTimeout(new Integer(waitTerminated.getAttributeValue("timeout")));
//    		}
//    		httpClient.setWaitingValue(waitTerminated.getText());
//
//    	}

	}

	public void setTimeout(int timeout){
		this.timeout = timeout;
	}

	public int getTimeout(){
		return timeout;
	}

	public void setFileNavigation(String fileNavigation) {
		this.fileNavigation= fileNavigation;
	}

	public String getFileNavigation() {
		return fileNavigation;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}


//	public class HttpParameter {
//
//		private String paramName = null;
//		private String paramValue = null;
//		private String paramFrom = null;
//
//		public HttpParameter(String paramName, String paramValue, String paramFrom) {
//			this.paramName = paramName;
//			this.paramValue = paramValue;
//			this.paramFrom = paramFrom;
//		}
//
//		public String getParamName() {
//			return paramName;
//		}
//
//		public void setParamName(String paramName) {
//			this.paramName = paramName;
//		}
//
//		public String getParamValue() {
//			return paramValue;
//		}
//
//		public void setParamValue(String paramValue) {
//			this.paramValue = paramValue;
//		}
//
//		public String getParamFrom() {
//			return paramFrom;
//		}
//
//		public void setParamFrom(String paramFrom) {
//			this.paramFrom = paramFrom;
//		}
//	}

	public String getOutputPrefix() {
		return outputPrefix;
	}

	public void setOutputPrefix(String outputPrefix) {
		this.outputPrefix = outputPrefix;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public boolean isStartConversation() {
		return startConversation;
	}

	public void setStartConversation(boolean startConversation) {
		this.startConversation = startConversation;
	}

	public String getWaitingQuery() {
		return waitingQuery;
	}

	public void setWaitingQuery(String waitingQuery) {
		this.waitingQuery = waitingQuery;
	}

	public String getWaitingTimeout() {
		return waitingTimeout;
	}

	public void setWaitingTimeout(String waitingTimeout) {
		this.waitingTimeout = waitingTimeout;
	}

	public boolean isWaitingTerminated() {
		return waitingTerminated;
	}

	public void setWaitingTerminated(boolean waitTerminated) {
		this.waitingTerminated = waitTerminated;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setUrlUpload(String urlUpload) {
		this.urlUpload = urlUpload;
	}

	public String getUrlUpload() {
		return urlUpload;
	}

	@Override
	public String getRepositoryName() {
		return Repository.WEB_NAVIGATION;
	}


    public Selenium executeSelenium(MemoryData memory, Selenium selenium) throws TestException, Exception{

    	Class seleniumClass=Class.forName(getFileNavigation());

    	SeleniumNavigation seleniumImpl = null;
            seleniumImpl = (SeleniumNavigation)	seleniumClass.getConstructor().newInstance();
        if (selenium!=null){
//            selenium.setSpeed(memory.getOrCreateRepository(Repository.FILE_PROPERTIES).getProperty("SELENIUM_SPEED"));
            seleniumImpl.setSelenium(selenium);
        }
        try{
            seleniumImpl.executeNavigation(memory);
        }catch(Exception e){

			logger.error("Eccezione durante la navigazione. ", e);
            String seleniumPath = memory.getOrCreateRepository(Repository.FILE_PROPERTIES).getProperty("SELENIUM_PATH");
            String errorDir = memory.getOrCreateRepository(Repository.FILE_PROPERTIES).getProperty(" SELENIUM_HTML_ERROR");
            if (seleniumPath==null){
                seleniumPath = "";
            }
       	    if (seleniumImpl!=null && seleniumImpl.getSelenium()!=null && seleniumImpl.getSelenium().getHtmlSource()!=null){
                   logger.info("Creazione del file di output di errore "+seleniumPath+getFileNavigation()+"_"+getNome().replace(' ', '_')+"_selenium.html");
       	           FileWriter fstream = new FileWriter(seleniumPath+errorDir+getFileNavigation()+"_"+getNome().replace(' ', '_')+"_selenium.html");
        	       BufferedWriter out = new BufferedWriter(fstream);
        	       out.write(seleniumImpl.getSelenium().getHtmlSource());
        	       out.close();
        	}

            String timeOpenSeleniumOnError = memory.getOrCreateRepository(Repository.FILE_PROPERTIES).getProperty("TIME_OPEN_SELENIUM_ON_ERROR");
            int wait = 10;
            if (timeOpenSeleniumOnError!=null){
                wait = new Integer(timeOpenSeleniumOnError).intValue();
            }
           logger.error("Exception during navigation. Stop "+(wait)+" seconds to visualize Selenium window");
           Thread.sleep((wait*1000));
           throw e;
        }

        waitTask(memory);

        return seleniumImpl.getSelenium();
    }

	private void waitTask(MemoryData memory) throws TestException{
		if (isWaitingTerminated()){
	    	boolean waitExit = false;
	    	int index = 0;
	    	int timeout = new Integer(getWaitingTimeout());
            IDatabaseConnection conn = new DatabaseConnection((Connection)memory.getObjectByName(getWaitingConnection()));
	    	while(!waitExit && (index<timeout)){
	    		String query = getWaitingQuery();
	    		logger.info("query "+query);
	    		ITable waiTaskItable = null;
	    		try{
	    			waiTaskItable = org.xmlblackbox.test.infrastructure.util.ITableUtil.getITable(conn,
	    				"WAITTASK", query);
	    		}catch(Exception e){
	    			logger.error("Exception ", e);
	    			throw new TestException("Eccezione eseguendo la query "+getWaitingQuery()+" per la WAIT_TERMINATED");

	    		}

	    		logger.info("waiTaskItable.getRowCount() "+waiTaskItable.getRowCount());
	    		if (waiTaskItable.getRowCount()==1){
	    			waitExit = true;
	    		}else{
	    			try {
	    	    		logger.info(index+" Sleep di un secondo prima di verificare se il batch ha terminato");
						Thread.sleep(1000);
						index++;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
	    		logger.info("waitExit "+waitExit);
	    		logger.info("(httpClient.getWaitingTimeout()) "+(getWaitingTimeout()));
	    		logger.info("(index<httpClient.getWaitingTimeout()) "+(index<timeout));

	    	}

		}
	}

	public HttpTestCaseSimple runHttpTester(WebNavigationSession conversation, Map resultVaribles, Properties prop, ClientWeb httpClient, MemoryData memory) throws TestException{

        HttpTestCaseSimple httpTestCase=
            new HttpTestCaseSimple();

        logger.info("httpClient.getNome() "+httpClient.getNome());
        logger.info("httpClient.getType() "+httpClient.getType());
        logger.info("httpClient.getApplication() "+httpClient.getApplication());
        logger.info("httpClient.getUrlUpload() "+httpClient.getUrlUpload());

        if(!httpClient.isStartConversation() && conversation!=null)
	        httpTestCase.setWebNavigationSession(conversation);

        Hashtable hParameters=new Hashtable();

        Iterator propertiesSet = prop.keySet().iterator();
        while(propertiesSet.hasNext()){
        	String propTemp = (String)propertiesSet.next();
        	hParameters.put(propTemp,prop.getProperty(propTemp));
        }

        logger.info("http.output.prefix "+httpClient.getOutputPrefix());
        hParameters.put("http.output.prefix",httpClient.getOutputPrefix());

        logger.info("hParameters "+hParameters);

        httpTestCase.setInitialVariables(hParameters);
        httpTestCase.addVariables(httpClient.getParameters());
        if (resultVaribles!=null){
        	httpTestCase.addVariables(resultVaribles);
        }
        httpTestCase.setNavigationFile(httpClient.getFileNavigation());
        httpTestCase.setTestClass(ClientWeb.class);
        try {
			httpTestCase.testNode();
		} catch (SystemException e) {
			logger.error("SystemException ", e);
			throw new TestException(e, "Eccezione");
		} catch (JiffieException e) {
			logger.error("JiffieException ", e);
			throw new TestException(e, "Eccezione");
		}


		waitTask(memory);

        return httpTestCase;

	}


	public HttpTestCaseSimple executeHttpClient(ClientWeb httpClient, HttpTestCaseSimple httpTestCase, MemoryData memory) throws Exception {
        try{
            Properties prop = memory.getOrCreateRepository(Repository.FILE_PROPERTIES);
           logger.info("Executed web client type "+getType());
           if (httpTestCase !=null){
           	return runHttpTester(httpTestCase.getWebConversation(),
           	httpTestCase.getResultVariables(), prop, httpClient, memory);
           }else{
        	   return runHttpTester(null, null, prop, httpClient,memory);
           }
        } catch(Exception e) {
              logger.error(e.getMessage(), e);
              throw new TestException("Errore durante la navigazione WEB ");
          }

	}

    /**
     * @return the waitingConnection
     */
    public String getWaitingConnection() {
        return waitingConnection;
    }

    /**
     * @param waitingConnection the waitingConnection to set
     */
    public void setWaitingConnection(String waitingConnection) {
        this.waitingConnection = waitingConnection;
    }

}