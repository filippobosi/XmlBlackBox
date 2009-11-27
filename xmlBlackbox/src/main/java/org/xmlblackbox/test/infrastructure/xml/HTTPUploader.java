
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
 *  &lt; HTTP-CLIENT nome="Login con SELENIUM"&gt; <br>
 *		&lt; TYPE name="WEB_SELENIUM"/&gt; <br>
 *		&lt; APPLICATION name="WEB"/&gt; <br>
 *		&lt; SELENIUM-NAVIGATION&gt;it.example.test.selenium.GenericLogin&lt; /SELENIUM-NAVIGATION&gt; <br>
 *		&lt; PARAMETERS&gt; <br>
 *			&lt; PARAMETER name="WEB_URL" value="${WEB_URL@testProp}" /&gt; <br>
 *			&lt; PARAMETER name="username" value="${USERNAME_COMUNE@testProp}"/&gt; <br>
 *			&lt; PARAMETER name="password" value="${PASSWORD_COMUNE@testProp}"/&gt; <br>
 *		&lt; /PARAMETERS&gt; <br>
 *	&lt; /HTTP-CLIENT&gt; <br><br>
 *
 *  Creazione della domanda economica
 * <br>
 *  &lt; HTTP-CLIENT nome="EsportaAnagrafica001 Crea domanda economica PRESA IN CARICO"&gt; <br>
 *		&lt; TYPE name="WEB_SELENIUM"/&gt; <br>
 *		&lt; APPLICATION name="WEB"/&gt; <br>
 *		&lt; OUTPUT_PREFIX&gt;EsportaAnagrafica001&lt; /OUTPUT_PREFIX&gt; <br>
 *		&lt; SELENIUM-NAVIGATION&gt;it.example.test.selenium.CreaDomandaEconomica&lt; /SELENIUM-NAVIGATION&gt; <br>
 *		&lt; PARAMETERS&gt; <br>
 *			&lt; PARAMETER name="click_button_finale" value="_finish_presenta"/&gt; <br>
 *			&lt; PARAMETER name="potenza_impegnata" value="3"/&gt; <br>
 *          &lt; PARAMETER name="codice_provincia_comune" value="${CODICE_PROVINCIA_COMUNE@testProp}"/&gt; <br>
 *			&lt; PARAMETER name="codice_comune" value="${CODICE_COMUNE@testProp}"/&gt; <br>
 *		&lt; /PARAMETERS&gt; <br>
 *	&lt; /HTTP-CLIENT&gt; <br>
 *
 *
 * @author acrea
 */
public class HTTPUploader extends Runnable  {
	
	private final static Logger logger = Logger.getLogger(HTTPUploader.class);

	public static String UPLOAD_FILE = "UPLOAD_FILE";
	
	private Map<String, String> parameters = new HashMap<String, String>();
	private String fileNavigation= null;
	private String urlLogin= null;
	private String urlUpload= null;
	private String fileToUpload = null;
	
	
	public HTTPUploader(Element el) throws Exception {
		super(el);
        build(el);
	}
	
	public void build(Element uploaderElement) throws Exception {
		parameters = new HashMap<String, String>();

        Element parametersElement = uploaderElement.getChild("PARAMETERS");
    	if (parametersElement!=null) {
    		Iterator parametersList = parametersElement.getChildren("PARAMETER", Namespace.getNamespace("http://www.xmlblackbox.org/xsd/")).iterator();
    		while (parametersList.hasNext()){
    			Element parameterElement = (Element) parametersList.next();
    			String pname = parameterElement.getAttributeValue("name");
    			String pvalue = parameterElement.getAttributeValue("value");
    			
    			parameters.put(pname, pvalue);
    		}
    	}
    	
		HTTPUploader httpUploader=this;
		logger.info("uploaderElement "+uploaderElement);
		logger.info("uploaderElement.getAttributeValue(nome) "+uploaderElement.getAttributeValue("name"));
		httpUploader.setNome(uploaderElement.getAttributeValue("name"));
		
    	Element fileInput= uploaderElement.getChild("FILE-TO-UPLOAD");
    	if (fileInput!=null){
    		httpUploader.setFileToUpload(fileInput.getText());
    	}

    	Element urlApload = uploaderElement.getChild("URL-UPLOAD");
		logger.info("urlApload "+urlApload);
    	if (urlApload!=null){
    		logger.info("urlApload.getText() "+urlApload.getText());
    		httpUploader.setUrlUpload(urlApload.getText());
    	}
    	
    	Element urlLogin = uploaderElement.getChild("URL-LOGIN");
		logger.info("urlLogin "+urlLogin);
    	if (urlLogin!=null){
    		logger.info("urlLogin.getText() "+urlLogin.getText());
    		httpUploader.setUrlLogin(urlLogin.getText());
    	}


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

	public void setUrlUpload(String urlUpload) {
		this.urlUpload = urlUpload;
	}

	public String getUrlUpload() {
		return urlUpload;
	}
	
    /**
     * @return the fileToUpload
     */
    public String getFileToUpload() {
        return fileToUpload;
    }

    /**
     * @param fileToUpload the fileToUpload to set
     */
    public void setFileToUpload(String fileToUpload) {
        this.fileToUpload = fileToUpload;
    }
    
	public String getUrlLogin() {
		return urlLogin;
	}

	public void setUrlLogin(String urlLogin) {
		this.urlLogin = urlLogin;
	}



	@Override
	public String getRepositoryName() {
		return Repository.WEB_NAVIGATION;
	}
	
	public void uploadFile(MemoryData memory, HTTPUploader httpClient) throws TestException, HttpException, IOException  {
		int ris;
		HttpClient hClient = new HttpClient();
		
		Properties prop = memory.getOrCreateRepository(getRepositoryName());
		logger.info("prop "+prop);

		
		/**
		 * Effettua la login
		 */
		String usernameStr = (String)httpClient.getParameters().get("username");
		String passwordStr = (String)httpClient.getParameters().get("password");

		Properties fileProperties = memory.getOrCreateRepository(Repository.FILE_PROPERTIES);
		
		logger.info("Login al "+urlLogin);
		PostMethod postMethodLogin = new PostMethod(urlLogin);
		NameValuePair username = new NameValuePair();
		username.setName("userId");
		username.setValue(usernameStr);
		logger.info("username "+usernameStr);
		NameValuePair password = new NameValuePair();
		password.setName("password");
		password.setValue(passwordStr);
		logger.info("password "+passwordStr);

        if (usernameStr!=null){
    		postMethodLogin.addParameter(username);
        }
        if (passwordStr!=null){
            postMethodLogin.addParameter(password);
        }
		ris = hClient.executeMethod(postMethodLogin);
		logger.debug("ris Login password "+passwordStr+" "+ris);

		if (ris != HttpStatus.SC_MOVED_TEMPORARILY) {
			throw new TestException("Error during login for uploading the file");
		}
		
		XmlObject[] domandeXml = null;
		
		File fileXML = new File(httpClient.getFileToUpload());
		PostMethod postm = new PostMethod(urlUpload);
		
		logger.debug("fileXML.getName() "+fileXML.getName());
		logger.debug("fileXML "+fileXML);
		logger.debug("postm.getParams()  "+postm.getParams());
		logger.debug("httpClient.getParameters().get(\"OPERATION_UPLOAD_NAME\") "+httpClient.getParameters().get("OPERATION_UPLOAD_NAME"));
		logger.debug("httpClient.getParameters().get(\"OPERATION_UPLOAD_VALUE\") "+httpClient.getParameters().get("OPERATION_UPLOAD_VALUE"));

		try {
                Part[] parts = {
					new StringPart(httpClient.getParameters().get("OPERATION_UPLOAD_NAME"), 
							httpClient.getParameters().get("OPERATION_UPLOAD_VALUE")),
					new FilePart("file", fileXML.getName(), fileXML
							)};

			postm.setRequestEntity(new MultipartRequestEntity(parts, postm.getParams()));
			logger.debug("parts "+parts);
		} catch (FileNotFoundException e2) {
			logger.error("FileNotFoundException ", e2);
			throw new TestException(e2, "FileNotFoundException ");
		}
		
		hClient.getHttpConnectionManager().getParams().setConnectionTimeout(8000);

		try {
			ris = hClient.executeMethod(postm);
			logger.info("ris Upload password "+passwordStr+" "+ris);
			logger.debug("ris Upload password "+passwordStr+" "+ris);
			if (ris == HttpStatus.SC_OK) {
				//logger.info("Upload completo, risposta=" + postm.getResponseBodyAsString());
				
				InputStream in = postm.getResponseBodyAsStream();
			    //OutputStream out = new FileOutputStream(new File(prop.getProperty("FILE_RISPOSTA_SERVLET")));
			    OutputStream out = new FileOutputStream(new File(httpClient.getFileOutput()));
			    OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
			    InputStreamReader reader = new InputStreamReader(in, "UTF-8");
			    
			    BufferedReader bufferedReader= new BufferedReader(reader);
			    
			    // Transfer bytes from in to out
			    //byte[] buf = new byte[1024];
			    //int len;
			    String linea = null;
			    while ((linea = bufferedReader.readLine()) !=null) {
			    	writer.write(linea);
			    }
			    writer.close();
			    reader.close();
			    in.close();
			    out.close();
			    
			} else {
			    logger.error("Upload failed, response =" + HttpStatus.getStatusText(ris));
			    logger.error("Exception : Server response not correct ");
			    throw new TestException("Exception : Server response not correct ");
			}
		} catch (HttpException e) {
		    logger.error("Exception : Server response not correct ", e);
		    throw new TestException(e, "");
		} catch (IOException e) {
		    logger.error("Exception : Server response not correct ", e);
	
		    throw new TestException(e, "");
		} finally {
			postm.releaseConnection();
		}
	}
}