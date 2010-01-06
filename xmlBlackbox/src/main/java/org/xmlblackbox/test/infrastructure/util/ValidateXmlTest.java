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

package org.xmlblackbox.test.infrastructure.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.xmlblackbox.test.infrastructure.exception.XmlValidationFault;
import org.xmlblackbox.xsd.TESTDocument;


public class ValidateXmlTest {

	private final static Logger log = Logger.getLogger(ValidateXmlTest.class);
	
	private InputStream xmlInputStream = null;
	private String message = "";
	
	
	public ValidateXmlTest(InputStream iS) {
		this.xmlInputStream = iS;
	}
	
	
	public void validate() throws XmlValidationFault{
		boolean ret = false;
		
		try {
		
			TESTDocument testDocument = TESTDocument.Factory.parse(xmlInputStream);
			
			if (!testDocument.validate()){
				throw new XmlValidationFault("Validation failed");
			}
			
		} catch (IOException e) {
			log.fatal("Validation exception ",e);
			throw new XmlValidationFault(e.getMessage());
		} catch (XmlException e) {
			log.fatal("Validation exception ",e);
			throw new XmlValidationFault(e.getMessage());
		}catch (Exception e) {
			log.fatal("Validation exception ",e);
			throw new XmlValidationFault(e.getMessage());
		}
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}



	
}
