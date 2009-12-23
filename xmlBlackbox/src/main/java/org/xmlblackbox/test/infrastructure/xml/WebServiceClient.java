package org.xmlblackbox.test.infrastructure.xml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.axis2.client.Stub;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.jdom.Element;
import org.xmlblackbox.test.infrastructure.interfaces.Repository;

/**
 * Es. Esegue la funzione PresaInCaricoLotto sulla classe Stub WebServiceStub inviando conme richiesta
 * il file template a cui vengono applicate le modifiche definite in EDIT_NODE
 * <br>
 * &lt;WEB-SERVICE-CLIENT nome="webservice.presaInCaricoEsportaAnagrafica001 "&lt;<br>
 *      &lt;FILE_INPUT&lt;target/EsportaAnagrafica001_2_in.xml.xml&lt;/FILE_INPUT&lt;<br
 *      &lt;FILE_OUTPUT&lt;target/EsportaAnagrafica001_2_out.xml&lt;/FILE_OUTPUT&lt;<br
 *      &lt;FILE_TEMPLATE&lt;src/test/resources/templateXml/WebServiceTemplate/RichiestaPresaInCaricoWebService.xml&lt;/FILE_TEMPLATE&lt;<br
 *      &lt;URL&lt;${WEB_SERVICE_URL@testProp}&lt;/URL&lt;<br
 *      &lt;STUB&lt;it.example.test.webservice.client.WebServiceStub&lt;/STUB&lt;<br
 *      &lt;OPERATION&lt;PresaInCaricoLotto&lt;/OPERATION&lt;<br
 * &lt;/WEB-SERVICE-CLIENT&lt;<br
 * @author Crea
 */
public class WebServiceClient extends Runnable {

    protected final static Logger logger = Logger.getLogger(WebServiceClient.class);
	
	private String url= null;
	private String stub= null;
	private String operation= null;

    HashMap<String, String> parameters = null;
	
//	private List insertCheck = new Vector();
	private List fileTemplate = new Vector();
	
	public WebServiceClient(Element el) throws Exception {
		super(el);
		build(el);
	}
	
	public void build(Element clientElement) throws Exception {
		logger.debug("clientElement "+clientElement);
		logger.debug("---------------------------------");
//		logger.debug(new XMLOutputter().outputString(xmlElement));
//		System.out.println(new XMLOutputter().outputString(xmlElement));
	    
		WebServiceClient wsClient=this;
		wsClient.setNome(clientElement.getAttributeValue("name"));
		logger.debug("wsClient.getNome() "+wsClient.getNome());
		

    	Element fileInput = clientElement.getChild("FILE-INPUT", uriXsd);
    	logger.debug("fileInput "+fileInput);
    	if (fileInput!=null){
    		wsClient.setFileInput(fileInput.getText());
    	}

    	Element urlServizioElement = clientElement.getChild("URL", uriXsd);

		logger.debug("urlServizioElement "+urlServizioElement);
    	if (urlServizioElement!=null){
    		wsClient.setUrl(urlServizioElement.getText());
    	}

    	Element stubServizioElement = clientElement.getChild("STUB", uriXsd);
		logger.debug("stubServizioElement "+stubServizioElement);
    	if (stubServizioElement!=null){
    		wsClient.setStub(stubServizioElement.getText());
    	}

    	
    	Element operationServizioElement = clientElement.getChild("OPERATION", uriXsd);
		logger.debug("operationServizioElement "+operationServizioElement);
    	if (operationServizioElement!=null){
    		wsClient.setOperation(operationServizioElement.getText());
    	}
    	
    	Element fileOutput = clientElement.getChild("FILE-OUTPUT", uriXsd);
    	logger.debug("fileOutput "+fileOutput);
    	if (fileOutput!=null){
        	wsClient.setFileOutput(fileOutput.getText());
    	}

        parameters = new HashMap();

        
    	parameters = parseParameters(clientElement);

	}

	public void setUrl(String urlServizio) {
		this.url = urlServizio;
	}

	public String getUrl(){
		return url;
	}

	public void setStub(String stubServizio) {
		this.stub = stubServizio;
	}

	public String getStub(){
		return stub;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getOperation(){
		return operation;
	}

    public HashMap<String, String> getParameters() {
        return parameters;
	}

	public void setParameters(HashMap<String, String> parameters) {
		this.parameters = parameters;
	}

	@Override
	public String getRepositoryName() {
		return Repository.WEB_SERVICE;
	}
	
	public XmlObject executeWebServiceClient() throws Exception
	{
        File fileInput = new File(getFileInput());
		
        XmlObject richiesta = XmlObject.Factory.parse(fileInput);

		XmlObject response=null;
		
        Stub binding = null;
        
//		fileInput = new File(fileInput.getParent()+"/"+testCaseName+fileInput.getName());
        
		logger.info("################ ");
		logger.info("Client WS: " + getNome());
		logger.info("URL: " + getUrl());
		logger.info("OPERATION: " + getOperation());
		logger.info("STUB: " + getStub());
		logger.info("FILEINPUT: " + fileInput);
		logger.info("################ ");
		
		  try{
              saveRichiesta(richiesta);
          }catch(Throwable e){
            logger.info ("Eccezione ", e);
          }
		
        logger.debug("executeWebServiceClient ");
        try {
        	Class stubClass=Class.forName(getStub());
        	
        	Class[] parametri=new Class[1];
        	parametri[0]=String.class;
        	
        	Object[] values=new Object[1];
        	values[0]=getUrl();
			logger.info("---- values : " +values[0]);
			logger.info("---- WS Operation     : " +getOperation());
        	
			binding = (Stub)
				stubClass.getConstructor(parametri).newInstance(values);
			
			logger.info("---- WS Operation     : " +getOperation());
			Class[] parametriInvocazione=new Class[1];
			
			for(int ipar=0; ipar < 1; ipar++){
				logger.info("ipar: " +ipar);
				logger.info("richiesta[ipar].xmlText(): " +richiesta.xmlText());
				
				Class[] interfacce=richiesta.getClass().getInterfaces();
				
				for (int i=0; i < interfacce.length; i++){
					if (XmlObject.class.isAssignableFrom(interfacce[i])){
						parametriInvocazione[0]=interfacce[i];
						logger.info("   Parametro invocazione #0 assegnato: "+parametriInvocazione[0]);
						break;
					}
				}
			}
			logger.info("---- WS Operation  ----------------");
			
			//Definisce il timeout del socket
//			String timeout = (String) prop.getProperty("timeoutInMilliSeconds");
//			if (timeout == null || timeout.equals("")) 
				
			String timeout ="300000";
			
			((org.apache.axis2.client.Stub)binding)._getServiceClient().getOptions().setTimeOutInMilliSeconds(Integer.parseInt(timeout));
			
			((org.apache.axis2.client.Stub)binding)._getServiceClient().getOptions().setProperty(HTTPConstants.CONNECTION_TIMEOUT, new Integer(1*240*1000));
			
        	Object valoriInvocazione=richiesta;

            logger.info("Invoke del Web Service getFileOutput() : "+getFileOutput());
        	logger.info("Invoke del Web Service richiesta : "+richiesta.xmlText());

        	Method metodo=stubClass.getDeclaredMethod(getOperation(), parametriInvocazione);
			logger.info("Invoke del Web Service ("+getOperation()+")");
            response=(XmlObject)metodo.invoke(binding, valoriInvocazione);
			logger.info("After the Web Service invoke ("+getOperation()+")");
			logger.info ("WS Resonse= [" +response.toString()+ "]");

        } catch (InvocationTargetException e) {
//        	try{
//        		logger.error("Fault", e);
//        		response=(XmlObject)e.getTargetException().getClass().getMethod("getFaultMessage", new Class[0]).invoke(e.getTargetException(), new Object[0]);
//        		saveRisposta(response);
//        		return response;
//        	}catch(NoSuchMethodException nsme){
//        		logger.error("Exception", nsme);
//        	}
        	logger.error("Exception", e);
        	throw e; 
        }
		
		saveRisposta(response);
        return response;

	}
	private void saveRisposta(XmlObject rispostaWebService) throws IOException, XmlException{
		
		logger.info("getFileOutput() "+ getFileOutput());
		File fileToSave = new File(getFileOutput());
		XmlOptions xmlOptions = new XmlOptions();
        xmlOptions.setSavePrettyPrint();
		rispostaWebService.save(new File(fileToSave.getParentFile()+"/"+fileToSave.getName()), xmlOptions);

	}
	
	private void saveRichiesta(XmlObject richiestaWebService) throws IOException, XmlException{
		
		logger.info("getFileInput() "+ getFileInput());
		File fileToSave = new File(getFileInput());
		XmlOptions xmlOptions = new XmlOptions();
        xmlOptions.setSavePrettyPrint();
		richiestaWebService.save(new File(fileToSave.getParentFile()+"/"+fileToSave.getName()), xmlOptions);
        

	}

    
    


}