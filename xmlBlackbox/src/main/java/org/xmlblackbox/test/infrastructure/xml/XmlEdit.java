package org.xmlblackbox.test.infrastructure.xml;

import java.io.StringReader;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.replacement.ReplaceFromXML;
import org.xmlblackbox.test.infrastructure.util.MemoryData;

import org.apache.log4j.Logger;

public class XmlEdit extends XmlElement implements InsertCheckList {

    protected final static Logger logger = Logger.getLogger(XmlEdit.class);
	private String target=null;
	
	private String namespace=null;
	
	private List listaCheck = new Vector();
	
	public XmlEdit(Element el) throws Exception {
		super(el);
		build(el);
	}
	
	public class InsertNode {
		
		public InsertNode(Element elemento) {
	    	this.setName(elemento.getAttributeValue("name"));
	    	this.setNamespace(elemento.getAttributeValue("namespace"));
		}
		
		private String name = null;
		private String namespace = null;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getNamespace() {
			return namespace;
		}
		public void setNamespace(String namespace) {
			this.namespace = namespace;
		}
	}
	
	public class RemoveNode {
		
		public RemoveNode(Element elemento) {
	    	this.setName(elemento.getAttributeValue("name"));
	    	this.setNamespace(elemento.getAttributeValue("namespace"));
		}
		
		private String name = null;
		private String namespace = null;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getNamespace() {
			return namespace;
		}
		public void setNamespace(String namespace) {
			this.namespace = namespace;
		}
	}

	
	public class InsertField {
		
		public InsertField(Element elemento) {
	    	this.setName(elemento.getAttributeValue("name"));
    		this.setValue(elemento.getAttributeValue("value"));
    		this.setNamespace(elemento.getAttributeValue("namespace"));
		}
		
		private String name=null;
		private String value=null;
		private String namespace=null;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
		public String getNamespace() {
			return namespace;
		}

		public void setNamespace(String namespace) {
			this.namespace = namespace;
		}
	}
	
	public void build(Element elemento) throws Exception {
		this.setName(elemento.getAttributeValue("name"));
    	this.setTarget(elemento.getAttributeValue("target"));
    	this.setNamespace(elemento.getAttributeValue("namespace"));
    	
        Iterator insertFieldList = elemento.getChildren("INSERT_FIELD").iterator();

        logger.info("insertFieldList.hasNext() "+insertFieldList.hasNext());

        while (insertFieldList.hasNext()){
        	
        	InsertField insertField = new InsertField((Element) insertFieldList.next());
            logger.info("insertField.getName() "+insertField.getName());
            logger.info("insertField.getNamespace() "+insertField.getNamespace());
            logger.info("insertField.getValue() "+insertField.getValue());

            listaCheck.add(insertField);
        }

        Iterator insertCheckList = elemento.getChildren("EDIT_NODE").iterator();
        
        while (insertCheckList.hasNext()){
        	XmlEdit insertCheck = new XmlEdit((Element) insertCheckList.next());
        	listaCheck.add(insertCheck);
        }

        Iterator insertNodeList = elemento.getChildren("INSERT_NODE").iterator();
        
        while (insertNodeList.hasNext()) {
        	InsertNode insertNode = new InsertNode((Element) insertNodeList.next());
        	listaCheck.add(insertNode);
        }
        
        Iterator removeNodeList = elemento.getChildren("REMOVE_NODE").iterator();
        
        while (removeNodeList.hasNext()) {
        	RemoveNode removeNode = new RemoveNode((Element) removeNodeList.next());
        	listaCheck.add(removeNode);
        }
	}
	
	
	public List getInsertCheck() {
		return listaCheck;
	}

	public void setInsertCheck(List listaCheck) {
		this.listaCheck = listaCheck;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public String getRepositoryName() {
		return Repository.REPO_NOT_SUPPORTED;
	}

	
}
