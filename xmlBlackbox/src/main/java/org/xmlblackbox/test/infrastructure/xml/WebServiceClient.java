package org.xmlblackbox.test.infrastructure.xml;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.jdom.Element;
import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.util.MemoryData;
import org.xmlblackbox.test.infrastructure.util.WebServiceClientManager;

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
		WebServiceClient wsClient=this;
		wsClient.setNome(clientElement.getAttributeValue("nome"));
		

    	Element urlServizioElement = clientElement.getChild("URL");
    	if (urlServizioElement!=null){
    		wsClient.setUrl(urlServizioElement.getText());
    	}

    	Element stubServizioElement = clientElement.getChild("STUB");
    	if (stubServizioElement!=null){
    		wsClient.setStub(stubServizioElement.getText());
    	}

    	Element operationServizioElement = clientElement.getChild("OPERATION");
    	if (operationServizioElement!=null){
    		wsClient.setOperation(operationServizioElement.getText());
    	}
        
    	Element fileInput = clientElement.getChild("FILE_INPUT");
    	wsClient.setFileInput(fileInput.getText());

    	Element fileOutput = clientElement.getChild("FILE_OUTPUT");
    	wsClient.setFileOutput(fileOutput.getText());

        parameters = new HashMap();

        Element parametersElement = clientElement.getChild("PARAMETERS");
    	if (parametersElement!=null) {
    		Iterator parametersList = parametersElement.getChildren("PARAMETER").iterator();
    		while (parametersList.hasNext()){
    			Element parameterElement = (Element) parametersList.next();
    			String pname = parameterElement.getAttributeValue("name");
    			String pvalue = parameterElement.getAttributeValue("value");
    			
    			parameters.put(pname, pvalue);
    		}
    	}

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
	
    public void eseguiWebService(MemoryData memory, int step) throws Exception{
        String fileInput = getFileInput();
        XmlObject richiesta = null;

        WebServiceClientManager webServiceClientManager =
            new WebServiceClientManager(this, memory.getOrCreateRepository(getRepositoryName()), getNome(), step);

            XmlObject richiestaTmp = XmlObject.Factory.parse(new File(fileInput));

//            doInsertCheck(webServiceClient, richiestaTmp, null);
            richiesta = richiestaTmp;
    		webServiceClientManager.executeWebServiceClient(richiesta);
    }
    
    


}