
package org.xmlblackbox.test.infrastructure.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

import org.apache.axis2.client.Stub;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import org.apache.xmlbeans.XmlOptions;
import org.xmlblackbox.test.infrastructure.xml.WebServiceClient;


public class WebServiceClientManager{
	
	private final static Logger log = Logger.getLogger(WebServiceClientManager.class);
	private WebServiceClient webServiceClient = null;
	private Properties prop = null;
	private String testCaseName = null;
	private XmlObject[] richiesta=null;
	private XmlObject response=null;
	private int step=-1;

	public WebServiceClientManager(WebServiceClient webServiceClient, Properties prop, String nomeTestCase, int step) throws Exception{
		this.webServiceClient = webServiceClient;
		this.prop = prop;
		this.testCaseName = nomeTestCase;		
		this.step = step;
		log.info("webServiceClient.getFileInput() "+webServiceClient.getFileInput());
		richiesta=new XmlObject[1];
	}

	
	public XmlObject executeWebServiceClient(XmlObject richiestaSingola) throws Exception
	{
        Stub binding = null;
        File fileInput = new File(webServiceClient.getFileInput());
        
		fileInput = new File(fileInput.getParent()+"/"+testCaseName+fileInput.getName());
        
		log.info("################ ");
		log.info("Client WS: " + webServiceClient.getNome());
		log.info("URL: " + webServiceClient.getUrl());
		log.info("OPERATION: " + webServiceClient.getOperation());
		log.info("STUB: " + webServiceClient.getStub());
		log.info("FILEINPUT: " + fileInput);
		log.info("################ ");
		
		richiesta[0] = richiestaSingola;

		  try{
              saveRichiesta(richiesta[0]);
          }catch(Throwable e){
            log.info ("Eccezione ", e);
          }
		
        log.debug("executeWebServiceClient ");
        try {
        	Class stubClass=Class.forName(webServiceClient.getStub());
        	
        	Class[] parametri=new Class[1];
        	parametri[0]=String.class;
        	
        	Object[] values=new Object[1];
        	values[0]=webServiceClient.getUrl();
			log.info("---- values : " +values[0]);
			log.info("---- WS Operation     : " +webServiceClient.getOperation());
        	
			binding = (Stub)
				stubClass.getConstructor(parametri).newInstance(values);
			
			log.info("---- WS Operation     : " +webServiceClient.getOperation());
			log.info("---- WS richiesta.length : " +richiesta.length);
			Class[] parametriInvocazione=new Class[richiesta.length];
			
			for(int ipar=0; ipar < richiesta.length; ipar++){
				log.info("ipar: " +ipar);
				log.info("richiesta[ipar].xmlText(): " +richiesta[ipar].xmlText());
				
				Class[] interfacce=richiesta[ipar].getClass().getInterfaces();
				
				for (int i=0; i < interfacce.length; i++){
					if (XmlObject.class.isAssignableFrom(interfacce[i])){
						parametriInvocazione[ipar]=interfacce[i];
						log.info("   Parametro invocazione #"+ipar+" assegnato: "+parametriInvocazione[ipar]);
						break;
					}
				}
			}
			log.info("---- WS Operation  ----------------");
			
			//Definisce il timeout del socket
			String timeout = (String) prop.getProperty("timeoutInMilliSeconds");
			if (timeout == null || timeout.equals("")) timeout="300000";
			
			((org.apache.axis2.client.Stub)binding)._getServiceClient().getOptions().setTimeOutInMilliSeconds(Integer.parseInt(timeout));
			
			((org.apache.axis2.client.Stub)binding)._getServiceClient().getOptions().setProperty(HTTPConstants.CONNECTION_TIMEOUT, new Integer(1*240*1000));
			
        	Object[] valoriInvocazione=richiesta;

            log.info("Invoke del Web Service webServiceClient.getFileOutput() : "+webServiceClient.getFileOutput());
        	log.info("Invoke del Web Service richiesta : "+richiesta[0].xmlText());


        	for (int i = 0; i < stubClass.getDeclaredMethods().length; i++) {
        		Method method = stubClass.getDeclaredMethods()[i];
            	log.info("method.getName() "+method.getName());
            	if(method.getParameterTypes().length>0)
            		log.info("method.getParameterTypes() "+method.getParameterTypes()[0]);
        		
			}
			Method metodo=stubClass.getDeclaredMethod(webServiceClient.getOperation(), parametriInvocazione);
			log.info("Invoke del Web Service ("+webServiceClient.getOperation()+")");
            response=(XmlObject)metodo.invoke(binding, valoriInvocazione);
			log.info("After the Web Service invoke ("+webServiceClient.getOperation()+")");
			log.info ("WS Resonse= [" +response.toString()+ "]");

        } catch (InvocationTargetException e) {
        	try{
        		log.error("Fault", e);
        		response=(XmlObject)e.getTargetException().getClass().getMethod("getFaultMessage", new Class[0]).invoke(e.getTargetException(), new Object[0]);
        		saveRisposta(response);
        		return response;
        	}catch(NoSuchMethodException nsme){
        		log.error("Exception", nsme);
        	}
        	log.error("Exception", e);
        	throw e; 
        }
		
		saveRisposta(response);
        return response;

	}
	private void saveRisposta(XmlObject rispostaWebService) throws IOException, XmlException{
		
		log.info("webServiceClient.getFileOutput() "+ webServiceClient.getFileOutput());
		File fileToSave = new File(webServiceClient.getFileOutput());
		XmlOptions xmlOptions = new XmlOptions();
        xmlOptions.setSavePrettyPrint();
		rispostaWebService.save(new File(fileToSave.getParentFile()+"/"+fileToSave.getName()), xmlOptions);

	}
	
	private void saveRichiesta(XmlObject richiestaWebService) throws IOException, XmlException{
		
		log.info("webServiceClient.getFileInput() "+ webServiceClient.getFileInput());
		File fileToSave = new File(webServiceClient.getFileInput());
		XmlOptions xmlOptions = new XmlOptions();
        xmlOptions.setSavePrettyPrint();
		richiestaWebService.save(new File(fileToSave.getParentFile()+"/"+fileToSave.getName()), xmlOptions);
        

	}
	
	public void addInputFile(XmlObject richiestaSAIADocument) throws IOException, XmlException {
		 OutputStreamWriter  osr = null;
		 OutputStream fos = null;
	     try{
			log.info("Salvo il file "+webServiceClient.getFileInput());
			String strFileInput = webServiceClient.getFileInput();
			File fileInput = new File(strFileInput);
		    
			String nomefile=fileInput.getName();
			String estensione=nomefile.substring(nomefile.lastIndexOf('.'));
			nomefile=nomefile.substring(0,nomefile.lastIndexOf('.'));
			
		    File fileOutput = new File(fileInput.getParent()+"/"+testCaseName+nomefile+"-"+(richiesta.length+1)+estensione);
			log.debug("fileInput.getParent() "+fileInput.getParent());
			log.debug("nomeTestCase "+testCaseName);
			log.debug("fileInput.getName() "+fileInput.getName());
			fos = new FileOutputStream(fileOutput);
		    osr = new OutputStreamWriter(fos, "UTF-8");
		    
			log.info("Salvo il file "+fileInput.getParent()+"/"+testCaseName+nomefile+"-"+(richiesta.length+1)+estensione);
	    	richiestaSAIADocument.save(osr);
	    	int i=0;
			while(richiesta[i]!=null){
				i++;
			}
				
	    	richiesta[i]=richiestaSAIADocument;
	    	
			log.info("Salvato il file "+fileInput.getParent()+"/"+testCaseName+nomefile+"-"+(richiesta.length+1)+estensione);
		}finally{
			if (osr!=null){
				osr.close();
			}
			if (fos!=null){
				fos.close();
			}
		}
	}
	

	public void rinominaOutput(WebServiceClient webServiceClient) throws IOException{
		InputStream in = new FileInputStream(new File((String)prop.getProperty("FILE_RISPOSTA_CLIENT")));
		//Al nome del file di output definito nel file xml del singolo test viene aggiunto il nome del test
		File fileOutput = new File(webServiceClient.getFileOutput());
		File file = new File(fileOutput.getParent()+"/"+testCaseName+fileOutput.getName());
		file.createNewFile();
		OutputStream out = new FileOutputStream(file);
	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	    log.debug("Rinominato il file di output ("+file.getName()+")");
	}
	

}


/*
 * $Id: $
 * $Log: $

 */