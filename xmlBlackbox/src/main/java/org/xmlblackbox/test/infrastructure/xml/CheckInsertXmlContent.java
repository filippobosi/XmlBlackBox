
package org.xmlblackbox.test.infrastructure.xml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.jdom.Element;
import org.jdom.Namespace;
import org.xmlblackbox.test.infrastructure.exception.XmlCheckException;
import org.xmlblackbox.test.infrastructure.exception.TestException;
import org.xmlblackbox.test.infrastructure.exception.XmlInsertException;
import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.util.MemoryData;

/**
 *
 * Es. Inserimento di un valore in un file xml attraverso XPath
 * <br>
 * &lt;INSERT-XML-CONTENT
 * fileinput="src/test/resources/templateXml/WebServiceTemplate/RichiestaPresaInCaricoWebServiceConIdUltimoLottoRicevuto.xml"
 * fileoutput="target/RichiestaPresaInCaricoWebServiceConIdUltimoLottoRicevuto_output.xml"
 * namespace="declare namespace xsd='http://www.example.it/xsd/';"&gt;<br>
 *     &lt;XML-INSERT-ROW name="VerificaFileXml"
 * xpath="$this//xsd:richiestaPresaInCaricoLotto/xsd:numeroMessaggi"
 * value="UUUUUUUUUUUAAAAAAAAA"/&gt;<br>
 *	&lt;/INSERT-XML-CONTENT&gt;<br>
 *
 * Es. Verifica della presenza di un valore in un file xml attraverso XPath
 * <br>
 *	&lt;CHECK-XML-CONTENT&gt;<br>
 *     &lt;XML-CHECK-ROW name="VerificaFileXml" filename="target/RichiestaPresaInCaricoWebServiceConIdUltimoLottoRicevuto_output.xml"
 * namespace="declare namespace xsd='http://www.example.it/xsd/';"
 * xpath="$this//xsd:richiestaPresaInCaricoLotto/xsd:numeroMessaggi"
 * value="UUUUUUUUUUUAAAAAAAAA"/&gt;<br>
 *	&lt;/CHECK-XML-CONTENT--&gt;<br>
 *
 *
 * @author Crea
 */

public class CheckInsertXmlContent extends Runnable {


	private final static Logger logger = Logger.getLogger(XmlInsertRow.class);

	private final static Logger log = Logger.getLogger(CheckInsertXmlContent.class);

    private Map<String, String> parameters = new HashMap<String, String>();

    private List  xmlList = new Vector();
    private String namespace;

	
	public CheckInsertXmlContent(Element el) throws Exception {
		super(el);
		build(el);
	}
	
	public void build(Element checkInsertXmlElement) throws Exception {
		log.info("checkInsertXmlElement "+checkInsertXmlElement);
		log.info("checkInsertXmlElement.getAttributeValue(nome) "+checkInsertXmlElement.getAttributeValue("name"));
		xmlList = new Vector();

        Element parametersElement = checkInsertXmlElement.getChild("PARAMETERS");
    	if (parametersElement!=null) {
    		Iterator parametersList = parametersElement.getChildren("PARAMETER", uriXsd).iterator();
    		while (parametersList.hasNext()){
    			Element parameterElement = (Element) parametersList.next();
    			String pname = parameterElement.getAttributeValue("name");
    			String pvalue = parameterElement.getAttributeValue("value");

    			parameters.put(pname, pvalue);
    		}
    	}

        setNome(checkInsertXmlElement.getAttributeValue("name"));
        setNamespace(checkInsertXmlElement.getAttributeValue("namespace"));
        setFileInput(checkInsertXmlElement.getAttributeValue("fileinput"));
        setFileOutput(checkInsertXmlElement.getAttributeValue("fileoutput"));

    	Iterator<Element> xmlCheckIterator = checkInsertXmlElement.getChildren("XML-CHECK-ROW", uriXsd).iterator();
    	while(xmlCheckIterator.hasNext()){
            Element element = xmlCheckIterator.next();
            if (element!=null){
                getCheckXmlList().add(getXmlCheckRow(element));
            }
        }

        Iterator<Element> xmlInsertIterator = checkInsertXmlElement.getChildren("XML-INSERT-ROW", uriXsd).iterator();
    	while(xmlInsertIterator.hasNext()){
            Element element = xmlInsertIterator.next();
            if (element!=null){
                getCheckXmlList().add(getXmlInsertRow(element));
            }
        }

        Iterator<Element> xmlInsertNodeIterator = checkInsertXmlElement.getChildren("XML-INSERT-NODE-ROW", uriXsd).iterator();
    	while(xmlInsertNodeIterator.hasNext()){
            Element element = xmlInsertNodeIterator.next();
            if (element!=null){
                getCheckXmlList().add(getXmlInsertNodeRow(element));
            }
        }

        Iterator<Element> xmlRemoveNodeIterator = checkInsertXmlElement.getChildren("XML-INSERT-NODE-ROW", uriXsd).iterator();
    	while(xmlRemoveNodeIterator.hasNext()){
            Element element = xmlRemoveNodeIterator.next();
            if (element!=null){
                getCheckXmlList().add(getXmlRemoveNodeRow(element));
            }
        }

	}

    private XmlCheckRow getXmlCheckRow(Element element){
        XmlCheckRow xmlRow = new XmlCheckRow();
        xmlRow.setNome(element.getAttributeValue("name"));
        xmlRow.setValue(element.getAttributeValue("value"));
        xmlRow.setFileName(getFileInput());
        xmlRow.setXPath(element.getAttributeValue("xpath"));
        xmlRow.setNamespace(getNamespace());
        xmlRow.setType(XmlCheckRow.TYPE);
        return xmlRow;

    }

    private XmlInsertRow getXmlInsertRow(Element element){
        XmlInsertRow xmlRow = new XmlInsertRow();
        xmlRow.setNome(element.getAttributeValue("name"));
        xmlRow.setValue(element.getAttributeValue("value"));
        xmlRow.setFileName(getFileInput());
        xmlRow.setXPath(element.getAttributeValue("xpath"));
        xmlRow.setOutputFileName(getFileOutput());
        xmlRow.setNamespace(getNamespace());
        xmlRow.setType(XmlInsertRow.TYPE);
        return xmlRow;

    }

    private XmlInsertRemoveNodeRow getXmlInsertNodeRow(Element element){
        XmlInsertRemoveNodeRow xmlRow = new XmlInsertRemoveNodeRow();
        xmlRow.setNome(element.getAttributeValue("name"));
        xmlRow.setFileName(getFileInput());
        xmlRow.setXPath(element.getAttributeValue("xpath"));
        xmlRow.setValue(element.getAttributeValue("value"));
        xmlRow.setOutputFileName(getFileOutput());
        xmlRow.setNamespace(getNamespace());
        xmlRow.setType(XmlInsertRemoveNodeRow.TYPE_INSERT);
        return xmlRow;

    }

    private XmlInsertRemoveNodeRow getXmlRemoveNodeRow(Element element){
        XmlInsertRemoveNodeRow xmlRow = new XmlInsertRemoveNodeRow();
        xmlRow.setNome(element.getAttributeValue("name"));
        xmlRow.setFileName(getFileInput());
        xmlRow.setXPath(element.getAttributeValue("xpath"));
        xmlRow.setValue(element.getAttributeValue("value"));
        xmlRow.setOutputFileName(getFileOutput());
        xmlRow.setNamespace(getNamespace());
        xmlRow.setType(XmlInsertRemoveNodeRow.TYPE_REMOVE);
        return xmlRow;

    }

    /**
     * @return the name
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param name the name to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }



	@Override
	public String getRepositoryName() {
		return Repository.SET_VARIABLE;
	}

    /**
     * @return the checkXmlList
     */
    public List getCheckXmlList() {
        return xmlList;
    }

    /**
     * @param checkXmlList the checkXmlList to set
     */
    public void setCheckXmlList(List checkXmlList) {
        this.xmlList = checkXmlList;
    }

    /**
     * @return the namespace
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * @param namespace the namespace to set
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    
    public XmlObject getXmlObject() throws TestException{
        XmlObject xobj = null;
        try {
            InputStream is = new FileInputStream(getFileInput());
            xobj = XmlObject.Factory.parse(is);
            return xobj;
        }
        catch (XmlException e) {
            logger.info("XmlException ", e);
            e.printStackTrace();
            throw new TestException();
        }
        catch (IOException e) {
            logger.info("IOException ", e);
            e.printStackTrace();
            throw new TestException();
        }
    }
    
    public XmlObject checkValueInXml(XmlObject xobj, XmlCheckRow xmlRow, MemoryData memory) throws Exception{

    	Properties prop = memory.getOrCreateRepository(getRepositoryName());
        XmlObject[] xmlObject = null;

        xmlObject = xobj.selectPath(xmlRow.getNamespace()+xmlRow.getXPath());

        logger.info("DoctypeName "+xobj.documentProperties().getDoctypeName());
        logger.info("DoctypePublicId "+xobj.documentProperties().getDoctypePublicId());


        logger.info("xmlObject "+xmlObject);
        if (xmlObject!=null && xmlObject.length==0){
            String error= "XmlObject not found searching by xpath query \""+xmlRow.getNamespace()+xmlRow.getXPath()+"\". Check the namespace.";
            throw new XmlCheckException(error);
        }
        logger.info("xmlObject.length "+xmlObject.length);
        logger.info("xmlObject[0] "+xmlObject[0]);
        logger.info("xmlObject[0].xmlText() "+xmlObject[0].xmlText());

        String value = xmlObject[0].newCursor().getTextValue();
        logger.info("xmlRow.getNome() "+xmlRow.getNome());
        logger.info("value "+value);
        logger.info("xmlRow.getValue() "+xmlRow.getValue());


        String errorMessage = xmlRow.getNome()+". The value contains in ("+xmlRow.getXPath()+") of the Xml file "
                    +xmlRow.getFileName()+" ("+value+") is not one expected ("+xmlRow.getValue()+")";

        if (!value.equals(xmlRow.getValue())){
            logger.info(errorMessage);
        }
        Assert.assertEquals(errorMessage, xmlRow.getValue(), value);

        return xobj;

    }
    
    public XmlObject insertValueInXml(XmlObject xobj, XmlInsertRow xmlRow, MemoryData memory) throws Exception{

    	Properties prop = memory.getOrCreateRepository(getRepositoryName());
        XmlObject[] xmlObject = null;
        logger.info("xmlRow.getNamespace() "+xmlRow.getNamespace());
        logger.info("xmlRow.getXPath() "+xmlRow.getXPath());

        logger.info("DoctypeName "+xobj.documentProperties().getDoctypeName());
        logger.info("DoctypePublicId "+xobj.documentProperties().getDoctypePublicId());

        xmlObject = xobj.selectPath(xmlRow.getNamespace()+xmlRow.getXPath());

        if (xmlObject.length<1){
            String error= "XmlObject not found searching by xpath query \""+xmlRow.getNamespace()+xmlRow.getXPath()+"\". Check the namespace.";
            throw new XmlInsertException(error);
        }

        logger.info("xobj 1 "+xobj);

//        xmlObject[0].newCursor().setAttributeText(new QName("xsi:nil"), null);
        xmlObject[0].newCursor().setTextValue(xmlRow.getValue());
        logger.info("xmlRow.getNome() "+xmlRow.getNome());
        logger.info("Inserito nel xml il valore "+xmlObject[0].newCursor().getTextValue());
        logger.info("XmlObject "+xmlObject[0].xmlText());

        logger.info("xobj 2 "+xobj);

        return xobj;
//        try{
//            logger.info("File output salvato "+xmlRow.getOutputFileName());
//            xobj.save(new File(xmlRow.getOutputFileName()));
//        }catch(IOException e){
//            throw new TestException(e, "IOException");
//        }
    }
    

    public XmlObject insertNodeInXml(XmlObject xobj, XmlInsertRemoveNodeRow xmlRow, MemoryData memory) throws TestException{

    	Properties prop = memory.getOrCreateRepository(getRepositoryName());
        XmlObject[] xmlObject = null;
//            InputStream is = new FileInputStream(xmlRow.getFileName());
//            xobj = XmlObject.Factory.parse(is);
        xmlObject = xobj.selectPath(xmlRow.getNamespace()+xmlRow.getXPath());
        logger.info("xobj : " + xobj.xmlText());
        logger.info("xmlRow.getNamespace()+xmlRow.getXPath() : " + xmlRow.getNamespace()+xmlRow.getXPath());


        logger.info("xmlRow.getValue(): " + xmlRow.getValue());
        String nodo = xmlRow.getValue();

        logger.info("xmlObject[0] : " + xmlObject[0].xmlText());
        XmlCursor cursor = xmlObject[0].newCursor();

        logger.info("Provvedo ad inserire un nuovo il nodo: " + nodo);

		StringTokenizer st = new StringTokenizer(nodo,"/");

        String[] parameter = null;

        logger.info("nodo.startsWith(\"$this\") " + nodo.startsWith("$this"));
        if (nodo.startsWith("$this")){
            parameter = new String[st.countTokens()-1];
        }else{
            parameter = new String[st.countTokens()];
        }

        logger.info("parameter.length: " + parameter.length);
        logger.info("st.countTokens(): " + st.countTokens());
        int index = -1;
        int countToken =st.countTokens();
		for (int i = 0;i<countToken;i++) {
            String token = st.nextToken();
			logger.info(i+" token " + token);
			logger.info(i+" token.equals(\"$this\") " + token.equals("$this"));
            if (!token.equals("$this")){
                index++;
    			parameter[index] = token;
    			logger.info("PARAMETER jj: " + parameter[index]);
                logger.info("i: " + i);
                logger.info("index: " + index);
                logger.info("PARAMETER: " + parameter[index]);
            }

		}
		cursor.toLastChild();

//        logger.info("xmlRow.getValue(): " + xmlRow.getValue());
//        cursor.beginElement(new QName("http://www.example.it/xsd/", "idUltimoLottoRicevutoCorrettamente"));

        logger.info("cursor.hasNextSelection(); " + cursor.hasNextSelection());
        logger.info("cursor.hasNextToken(): " + cursor.hasNextToken());
        logger.info("cursor.hasPrevToken(): " + cursor.hasPrevToken());
        logger.info("cursor.hasNextToken(): " + cursor.hasNextToken());


		for (int i = 0;i<parameter.length;i++) {
            logger.info("beginElement parameter["+i+"]: " + parameter[i]);
            cursor.beginElement(parameter[i], "http://www.example.it/xsd/");
		}

		cursor.dispose();
//        cursor.setTextValue("");
        logger.debug(" xobj.xmlText() "+xobj.xmlText());

        return xobj;
//        try{
//            logger.info("File output salvato "+xmlRow.getOutputFileName());
//            xobj.save(new File(xmlRow.getOutputFileName()));
//        }catch(IOException e){
//            throw new TestException(e, "IOException");
//        }

    }





}
