

package org.xmlblackbox.test.infrastructure.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dbunit.database.IDatabaseConnection;
import org.jdom.Element;
import org.jdom.Namespace;
import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.util.DBConnection;

public class XmlDBConnection extends XmlElement{

    private final static Logger logger = Logger.getLogger(XmlDBConnection.class);

	private IDatabaseConnection conn= null;
	private String nome = null;
	private Map<String, String> parameters = new HashMap<String, String>();

	public XmlDBConnection(Element el) throws Exception {
		super(el);
		build(el);
	}
	
	public void build(Element dbcheckElement) throws Exception{
		
		XmlDBConnection dbCconnection = this;
		
		dbCconnection.setNome((String)dbcheckElement.getAttributeValue("name"));

        Element parametersElement = dbcheckElement.getChild("PARAMETERS");
    	if (parametersElement!=null) {
    		Iterator parametersList = parametersElement.getChildren("PARAMETER", uriXsd).iterator();
    		while (parametersList.hasNext()){
    			Element parameterElement = (Element) parametersList.next();
    			String pname = parameterElement.getAttributeValue("name");
    			String pvalue = parameterElement.getAttributeValue("value");

    			parameters.put(pname, pvalue);
    		}
    	}
	}
	
	public IDatabaseConnection getConnection() throws Exception{
		// Connessione al DB...
		DBConnection.setConfig(DBConnection.params.DRIVER, parameters.get("db.driver"));
		DBConnection.setConfig(DBConnection.params.URL, parameters.get("db.url"));
		DBConnection.setConfig(DBConnection.params.USERNAME, parameters.get("db.user"));
		DBConnection.setConfig(DBConnection.params.PASSWORD, parameters.get("db.pw"));
		conn = DBConnection.getConnection();
		logger.debug("[DB Connection][OK]");

		return conn;
		
	}
	
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}
	@Override
	public String getRepositoryName() {
		return Repository.DB_CONNECTION;
	}
	
}
