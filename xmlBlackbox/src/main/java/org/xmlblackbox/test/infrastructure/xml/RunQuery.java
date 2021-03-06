/**
 *
 * This file is part of XmlBlackBox.
 *
 * XmlBlackBox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * XmlBlackBox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with XmlBlackBox.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.xmlblackbox.test.infrastructure.xml;

import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.replacement.ReplaceFromXML;
import org.xmlblackbox.test.infrastructure.util.MemoryData;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.dbunit.dataset.DataSetException;
import org.jdom.Element;
import org.jdom.Namespace;
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
public class RunQuery extends XmlElement{


	private final static Logger log = Logger.getLogger(RunQuery.class);
    private Map<String, String> parameters = new HashMap<String, String>();

    private String connection;

    private List queryList = new Vector();


	public RunQuery(Element el) throws Exception {
		super(el);
		build(el);
	}
	
	public void build(Element executeQueryElement) throws DataSetException, IOException{
		log.info("executeQueryElement "+executeQueryElement);
		log.info("executeQueryElement.getAttributeValue(nome) "+executeQueryElement.getAttributeValue("name"));
		log.info("executeQueryElement.getChildren(\"QUERY\").size() "+executeQueryElement.getChildren("QUERY").size());
		queryList = new Vector();

		
//        Element parametersElement = executeQueryElement.getChild("PARAMETERS");
//    	if (parametersElement!=null) {
//    		Iterator parametersList = parametersElement.getChildren("PARAMETER", uriXsd).iterator();
//    		while (parametersList.hasNext()){
//    			Element parameterElement = (Element) parametersList.next();
//    			String pname = parameterElement.getAttributeValue("name");
//    			String pvalue = parameterElement.getAttributeValue("value");
//
//    			parameters.put(pname, pvalue);
//    		}
//    	}
    	parameters = parseParameters(executeQueryElement);

    	Iterator<Element> queryIterator = executeQueryElement.getChildren("QUERY", uriXsd).iterator();
    	while(queryIterator.hasNext()){
            Element executeQuery = queryIterator.next();
			log.info("executeQueryElement.getAttributeValue(nome) "+executeQuery.getAttributeValue("nome"));
    		log.info("executeQueryElement.getAttributeValue(query) "+executeQuery.getAttributeValue("query"));
            log.info("executeQueryElement.getAttributeValue(type) "+executeQuery.getAttributeValue("type"));
            log.info("executeQueryElement.getAttributeValue(connection) "+executeQuery.getAttributeValue("connection"));

            if (executeQuery!=null){
                Query query = new Query();
                query.setNome(executeQuery.getAttributeValue("name"));
                query.setQuery(executeQuery.getAttributeValue("query"));
                query.setType(executeQuery.getAttributeValue("type"));
                query.setConnection(executeQuery.getAttributeValue("connection"));


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

    /**
     * @return the connection
     */
    public String getConnection() {
        return connection;
    }

}
