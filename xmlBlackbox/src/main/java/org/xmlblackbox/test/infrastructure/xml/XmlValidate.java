package org.xmlblackbox.test.infrastructure.xml;

import java.util.Iterator;

import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.util.ValidateXML;

import org.jdom.Element;

public class XmlValidate extends XmlElement {

	private String fileXml = null;
	private String fileXsd = null;
		
	public String getFileXml() {
		return fileXml;
	}

	public void setFileXml(String fileXml) {
		this.fileXml = fileXml;
	}

	public String getFileXsd() {
		return fileXsd;
	}

	public void setFileXsd(String fileXsd) {
		this.fileXsd = fileXsd;
	}

	public XmlValidate(Element el) throws Exception {
		super(el);
		build(el);
	}
	
	@Override
	public void build(Element xmlElement) throws Exception {
		setFileXml(xmlElement.getAttributeValue("xml"));
		setFileXsd(xmlElement.getAttributeValue("xsd"));
	}

	@Override
	public String getRepositoryName() {
		return Repository.REPO_NOT_SUPPORTED;
	}
	
    public void executeValidazioneXml() throws Exception{
    	ValidateXML validatore = new ValidateXML(getFileXml(),getFileXsd());
    	validatore.validate();
    }


}
