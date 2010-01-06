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

package org.xmlblackbox.test.infrastructure.replacement;

import org.xmlblackbox.test.infrastructure.exception.InvalidVariableAnnotation;
import org.xmlblackbox.test.infrastructure.exception.RepositoryNotFound;
import org.xmlblackbox.test.infrastructure.exception.TestException;
import org.xmlblackbox.test.infrastructure.exception.VariableNotFound;
import org.xmlblackbox.test.infrastructure.util.MemoryData;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;

public class ReplaceFromXML extends ReplaceString {

	private final static Logger log = Logger.getLogger(ReplaceFromXML.class);
	private boolean continueIfNoData=false;
	
	
	public ReplaceFromXML(Map source) {
		super(source);
	}
	
	public ReplaceFromXML(MemoryData source, String startDelimiter, String endDelimiter) {
		super(source, startDelimiter, endDelimiter);
	}
	
	public ReplaceFromXML(Map source, String startDelimiter, String endDelimiter) {
		super(source, startDelimiter, endDelimiter);
	}

	public ReplaceFromXML(Map source, String startDelimiter, String endDelimiter, boolean continueIfNoData) {
		super(source, startDelimiter, endDelimiter);
		setContinueIfNoData(continueIfNoData);
	}
	
	public String getReplacementValue(String value) throws Exception {
		return getReplacementValue(source, Replacer.MAIN_SOURCE, value);
	}
	
	public String getReplacementValue(MemoryData source, String sourceName, String value) throws Exception  {
		String valoreVariabile  = null;
		try {
			valoreVariabile = source.get(value);
			log.info("[-] "+ value + " = " + valoreVariabile);
		} catch (RepositoryNotFound e) {
			log.error("[!] Replace fallito : " + e.getClass().getSimpleName() + " " + value);
			throw e;
		} catch (InvalidVariableAnnotation e) {
			log.error("[!] Replace fallito : " + e.getClass().getSimpleName() + " " + value);
			throw e;
		} catch (VariableNotFound e) {
			log.error("[!] Replace fallito : " + e.getClass().getSimpleName() + " " + value);
			throw new TestException( e, e.getMessage());
		}
		return valoreVariabile;
	}
	
	/*
	public String getReplacementValue(Object source, String sourceName, String value) throws Exception {
		String retrievedValue = "";
		String checkValue=value;
		log.info("newSourceName "+value.substring(value.lastIndexOf("@")+1));
		log.info("source "+source);
		log.info("value "+value);
		log.info("value.indexOf(@) "+value.indexOf("@"));
		
		if (value.indexOf("@")>0) {
			if (value.lastIndexOf("@")+1 == value.length()) {
				throw new Exception ("Malformed data provider name "+value+"");
			}
			String newSourceName = value.substring(value.lastIndexOf("@")+1);
			value = value.substring(0, value.lastIndexOf("@"));
			Map map = (Map) source; 
			log.info("newSourceName "+newSourceName);
			log.info("value "+value);
			log.info("source "+source);
			
			if (map.containsKey(newSourceName)) {
				retrievedValue = getReplacementValue(map.get(newSourceName), newSourceName, value);
			} else if(isContinueIfNoData()){
				retrievedValue=checkValue;
			} else {
				System.out.println("--------------");
				System.out.println("DOMAIN SET IS");
				Set set = map.keySet();
				Iterator it = set.iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					System.out.println("key:"+key);
				}
				System.out.println("--------------");
				throw new Exception ("Source "+ newSourceName +" not found in source "+sourceName);
			}
		} else {
			if (XmlObject.class.isAssignableFrom(source.getClass())) {
				XmlObject nodo = (XmlObject) source;
				XmlObject[] nodi = nodo.execQuery(m_namespaceDeclaration + value);
				if (nodi.length == 0) {
					throw new Exception ("Value "+ value +" not found in source "+sourceName);
				} else if (nodi.length > 1) {
					throw new Exception("Trovati più nodi corrispondenti al path fornito ("+value+") in "+sourceName);
					
				}
				retrievedValue = nodi[0].newCursor().getTextValue();
			} else if (Map.class.isAssignableFrom(source.getClass())) {
				Map map = (Map) source; 
				System.out.println("-------------- map "+map);
				System.out.println("-------------- value "+value);
				System.out.println("-------------- map.containsKey(value) "+map.containsKey(value));
				if (map.containsKey(value)) {
					retrievedValue = (String) map.get(value);
				} else if(isContinueIfNoData()){
					retrievedValue=value;
				} else {
					throw new Exception ("Value "+ value +" not found in source "+sourceName);
				}
			} else {
				throw new Exception ("Unexpected class type ("+source.getClass()+") for "+ value);
			}
		}
		return retrievedValue;
	}
	*/

	public boolean isContinueIfNoData() {
		return continueIfNoData;
	}

	public void setContinueIfNoData(boolean continueIfNoData) {
		this.continueIfNoData = continueIfNoData;
	}

	@Override
	public String getReplacementValue(Object source, String sourceName,String value) throws Exception {
		return getReplacementValue((MemoryData) source, sourceName, value);
	}

}
