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

import org.xmlblackbox.test.infrastructure.exception.TestException;
import java.io.IOException;

import java.sql.SQLException;
import org.apache.xmlbeans.XmlException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.jdom.Element;
import org.apache.log4j.Logger;


import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlObject;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.ITable;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;
import org.xmlblackbox.test.infrastructure.exception.SetVariableException;
import org.xmlblackbox.test.infrastructure.util.MemoryData;

/**
 * Es. definisce una variabile (da usare nei passi successivi del test) prendendo
 * un valore o un altra variabile.
 * <br>
 *&lt;SET-VARIABLE&gt;<br>
 *  &lt;SET name="idDomandaSospesa" value="${idDomanda@selenium}" type="VALUE_TYPE"/&gt;<br>
 *&lt;/SET-VARIABLE&gt;<br>
 * Il valore puo' essere anche preso da un file xml con XPath
 *     &lt;SET-VARIABLE&gt;<br>
 *       &lt;SET name="idLotto" filename="target/EsportaAnagrafica001_2_out.xml"
 *           namespace="declare namespace xsd='http://www.example.it/xsd/';"
 *          xpath="$this//xsd:rispostaPresaInCaricoLotto/xsd:lottoMessaggi/xsd:idLotto"
 *          type="XML_TYPE"/&gt;<br>
 *    &lt;/SET-VARIABLE&gt;<br>
 *

 *
 * @author Crea
 */
public class SetVariable extends XmlElement{


	private final static Logger log = Logger.getLogger(SetVariable.class);

    private List  setList = new Vector();

	public SetVariable(Element setVarElement) throws Exception {
		super(setVarElement);
		build(setVarElement);
	}
	
	public void build(Element setVarElement) throws Exception {
		log.info("setVarElement "+setVarElement);
		log.info("setVarElement.getAttributeValue(nome) "+setVarElement.getAttributeValue("name"));
		setList = new Vector();
        log.info("SET-VARIABLE build element2 "+new XMLOutputter().outputString(setVarElement));

    	Iterator<Element> setIterator = setVarElement.getChildren("SET", uriXsd).iterator();
        log.info("setIterator "+setVarElement.getChildren("SET", uriXsd).size());

        if (!setIterator.hasNext()){
            throw new TestException("SET tag not found");
        }

    	while(setIterator.hasNext()){
            Element setVariable = setIterator.next();
            if (setVariable!=null){
                Set set = new Set();
                set.setNome(setVariable.getAttributeValue("name"));
                set.setValue(setVariable.getAttributeValue("value"));
                set.setType(setVariable.getAttributeValue("type"));
                set.setFileName(setVariable.getAttributeValue("filename"));
                set.setNamespace(setVariable.getAttributeValue("namespace"));
                set.setXPath(setVariable.getAttributeValue("xpath"));
                set.setQuery(setVariable.getAttributeValue("query"));
                set.setConnection(setVariable.getAttributeValue("connection"));


                getSetList().add(set);
            }
        }
	}
	
	@Override
	public String getRepositoryName() {
		return Repository.SET_VARIABLE;
	}

    /**
     * @return the setList
     */
    public List getSetList() {
        return setList;
    }

    /**
     * @param setList the setList to set
     */
    public void setSetList(List setList) {
        this.setList = setList;
    }

    public static  void setVariableFromXml(Set set, Properties prop) throws Exception {


        XmlObject[] xmlObject = null;
        XmlObject xobj = null;
        try {
            InputStream is = new FileInputStream(set.getFileName());
            xobj = XmlObject.Factory.parse(is);
            xmlObject = xobj.selectPath(set.getNamespace()+set.getXPath());

            if (xmlObject.length == 0){
                String error= "XmlObject not found searching by xpath query \""+set.getNamespace()+set.getXPath()+"\". Check the namespace.";
                throw new SetVariableException(error);
            }


        }
        catch (XmlException e) {
            log.info("XmlException ", e);
            e.printStackTrace();
            throw new TestException();
        }
        catch (IOException e) {
            log.info("IOException ", e);
            e.printStackTrace();
            throw new TestException();
        }

        String value = xmlObject[0].newCursor().getTextValue();
        log.info("set.getNome() "+set.getNome());
        log.info("value "+value);

        prop.setProperty(set.getNome(), value);


    }

    public static void setVariableFromDb(Set currentSet, MemoryData memory) throws TestException {
        try {

            Properties prop = memory.getOrCreateRepository(Repository.SET_VARIABLE);
            IDatabaseConnection conn = new DatabaseConnection((Connection)memory.getConnectionByName(currentSet.getConnection()));

            log.info("query: " + currentSet.getQuery());
            log.info("conn: " + conn);
            log.info("currentSet: " + currentSet);
            log.info("currentSet.getConnection(): " + currentSet.getConnection());
            log.info("conn.getConnection(): " + conn.getConnection());
            PreparedStatement prepareStatement = conn.getConnection().prepareStatement(currentSet.getQuery());
            ResultSet resultSet= prepareStatement.executeQuery();
            if (resultSet.next()){
                log.info("result: " + resultSet.getString(1));
                prop.setProperty(currentSet.getNome(), resultSet.getString(1));
                log.info("valore estratto dal DB " + resultSet.getString(1));
            } else {
                throw new TestException("Nessun risultato per set " + currentSet.getNome());
            }
            if (resultSet.next()) {
                throw new TestException("Piu' di un risultato per set " + currentSet.getNome());
            }
        }  catch (SQLException ex) {
            log.info("SQLException ", ex);
            ex.printStackTrace();
            throw new TestException();
        }



    }

}