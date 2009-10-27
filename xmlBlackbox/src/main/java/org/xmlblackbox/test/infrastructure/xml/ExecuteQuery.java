/* Generated by Together */

package org.xmlblackbox.test.infrastructure.xml;

import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.replacement.ReplaceFromXML;
import org.xmlblackbox.test.infrastructure.util.MemoryData;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.dbunit.dataset.DataSetException;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
/**
 * Es. Esegue una query
 * <br>
 *     &lt;EXECUTE-QUERY nome="CambioStatoDomanda"&gt;<br>
 *        &lt;QUERY name="nome di questo tag" query="UPDATE RICHIESTA_VARIAZ SET STATO_RICHIESTA='presaInCarico' WHERE ID_DOMANDA_AGEV=${idDomandaPresaInCarico@setVariable}" type="UPDATE"/&gt;<br>
 *    &lt;/EXECUTE-QUERY&gt;<br>
 *
 * @author Crea
 */
public class ExecuteQuery extends XmlElement{


	private final static Logger log = Logger.getLogger(ExecuteQuery.class);
    private Map<String, String> parameters = new HashMap<String, String>();


    private List queryList = new Vector();


	public ExecuteQuery(Element el) throws Exception {
		super(el);
		build(el);
	}
	
	public void build(Element executeQueryElement) throws DataSetException, IOException{
		log.info("executeQueryElement "+executeQueryElement);
		log.info("executeQueryElement.getAttributeValue(nome) "+executeQueryElement.getAttributeValue("nome"));
		log.info("executeQueryElement.getChildren(\"QUERY\").size() "+executeQueryElement.getChildren("QUERY").size());
		queryList = new Vector();

        Element parametersElement = executeQueryElement.getChild("PARAMETERS");
    	if (parametersElement!=null) {
    		Iterator parametersList = parametersElement.getChildren("PARAMETER").iterator();
    		while (parametersList.hasNext()){
    			Element parameterElement = (Element) parametersList.next();
    			String pname = parameterElement.getAttributeValue("name");
    			String pvalue = parameterElement.getAttributeValue("value");

    			parameters.put(pname, pvalue);
    		}
    	}

    	Iterator<Element> queryIterator = executeQueryElement.getChildren("QUERY").iterator();
    	while(queryIterator.hasNext()){
            Element executeQuery = queryIterator.next();
			log.info("executeQueryElement.getAttributeValue(nome) "+executeQuery.getAttributeValue("nome"));
    		log.info("executeQueryElement.getAttributeValue(query) "+executeQuery.getAttributeValue("query"));
            log.info("executeQueryElement.getAttributeValue(type) "+executeQuery.getAttributeValue("type"));

            if (executeQuery!=null){
                Query query = new Query();
                query.setNome(executeQuery.getAttributeValue("name"));
                query.setQuery(executeQuery.getAttributeValue("query"));
                query.setType(executeQuery.getAttributeValue("type"));


                getQueryList().add(query);
            }
        }
	}
	
	@Override
	public String getRepositoryName() {
		return Repository.SET_VARIABLE;
	}

    /**
     * @return the queryList
     */
    public List getQueryList() {
        return queryList;
    }

    /**
     * @param queryList the queryList to set
     */
    public void setQueryList(List queryList) {
        this.queryList = queryList;
    }

}