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

import org.xmlblackbox.test.infrastructure.exception.TestException;

import org.apache.log4j.Logger;


public abstract class ReplaceString implements Replacer {

	private final static Logger log = Logger.getLogger(ReplaceString.class);
	
	public String startDelimiter = null;
	public String endDelimiter = null;
	public Object source = null;
	
	public ReplaceString(Object source) {
		this.source = source;
		startDelimiter = null;
		endDelimiter = null;
	}

	public ReplaceString(Object source, String startDelimiter, String endDelimiter) {
		this.source = source;
		this.startDelimiter = startDelimiter;
		this.endDelimiter = endDelimiter;
	}

	public String replace(String text) throws Exception {
		log.debug("- Replace delimiter { START : " + startDelimiter + " END : " + endDelimiter + " }");
		if (startDelimiter == null || startDelimiter.equals("") ||
			endDelimiter == null || endDelimiter.equals("")) {
			throw new Exception("Delimitatori non definiti!");
		}
		
		if (text.indexOf(startDelimiter) < 0) {
			return text;
		} if(text.substring( text.indexOf(startDelimiter)+startDelimiter.length(),text.indexOf(endDelimiter)).length()==0){
			
			return text.substring(0, text.indexOf(startDelimiter)) + 
					startDelimiter +
					endDelimiter +
			   		replace(text.substring(text.indexOf(endDelimiter)+endDelimiter.length()));
			
		}else {
			
			return text.substring(0, text.indexOf(startDelimiter)) + 
				   getReplacementValue(source, 
						   			   Replacer.MAIN_SOURCE,
						   			   text.substring( text.indexOf(startDelimiter)+startDelimiter.length(), 
														text.indexOf(endDelimiter))) +
				   replace(text.substring(text.indexOf(endDelimiter)+endDelimiter.length()));
		}
	}
	
	public abstract String getReplacementValue(Object source, String sourceName, String value) throws Exception;

	public String getEndDelimiter() {
		return endDelimiter;
	}

	public void setEndDelimiter(String endDelimiter) {
		this.endDelimiter = endDelimiter;
	}

	public String getStartDelimiter() {
		return startDelimiter;
	}

	public void setStartDelimiter(String startDelimiter) {
		this.startDelimiter = startDelimiter;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}
}
