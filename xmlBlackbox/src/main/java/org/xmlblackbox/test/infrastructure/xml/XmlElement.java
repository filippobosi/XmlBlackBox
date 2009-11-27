package org.xmlblackbox.test.infrastructure.xml;

import org.xmlblackbox.test.infrastructure.replacement.ReplaceFromXML;
import org.xmlblackbox.test.infrastructure.util.MemoryData;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.dbunit.dataset.DataSetException;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public abstract class XmlElement {
	
	
	public XmlElement(Element el) {
		super();
		this.xmlElement = el;
		this.xmlTagName = el.getName();
	}

	protected Element xmlElement=null;
	private String xmlTagName=null;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getXmlTagName() {
		return xmlTagName;
	}

	private final static Logger log = Logger.getLogger(XmlElement.class);
    
	public void reload(MemoryData memoryData) throws Exception{
		log.info("[Reload variables]");
		ReplaceFromXML replacer= new ReplaceFromXML(memoryData,"${","}");
		log.debug("- PRIMA -------------------------------------------");
		log.debug("- xmlElement "+xmlElement);
		
		log.debug(new XMLOutputter().outputString(xmlElement));
		String sReplaced=replacer.replace(new XMLOutputter().outputString(xmlElement));
		Element replaced=new SAXBuilder(false).build(new StringReader(sReplaced)).getRootElement();
		log.debug("- DOPO --------------------------------------------");
		log.debug("- replaced "+replaced);
		log.debug(new XMLOutputter().outputString(replaced));
	    build(replaced);
	    log.info("[Reload variables][OK]");
	}
	
	public abstract void build(Element checkInsertXmlElement) throws Exception;

	public abstract String getRepositoryName();
	protected HashMap parseParameters(Element parameters){
		HashMap<String,String> params = new HashMap<String, String>();

		if (parameters!=null) {
			Iterator parametersList = parameters.getChildren("PARAMETER", Namespace.getNamespace("http://www.xmlblackbox.org/xsd/")).iterator();
			while (parametersList.hasNext()){
				Element parameterElement = (Element) parametersList.next();
				String pname = parameterElement.getAttributeValue("name");
				String pvalue = parameterElement.getAttributeValue("value");
				
				params.put(pname, pvalue);
			}
		}
		return params;
	}
	
}
