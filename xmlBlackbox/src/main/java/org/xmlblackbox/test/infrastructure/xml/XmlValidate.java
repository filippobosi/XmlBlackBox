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

import java.util.Iterator;

import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.util.ValidateXML;
import org.xmlblackbox.test.infrastructure.util.ValidateXmlTest;

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
    	ValidateXML validatore = new ValidateXML(getFileXml(), getFileXsd());
    	validatore.validate();
    }


}
