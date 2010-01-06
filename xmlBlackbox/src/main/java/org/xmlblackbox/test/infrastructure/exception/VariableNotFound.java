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

package org.xmlblackbox.test.infrastructure.exception;

import java.util.Hashtable;

import org.apache.log4j.Logger;

public class VariableNotFound extends Exception {
	
    protected final static Logger log = Logger.getLogger(VariableNotFound.class);

	public VariableNotFound(String key,Hashtable repository) {
		super("[!] Variable not found : "+key);
		log.error("---------------------------------------------------------");
		log.error("[!] Variable not found : " + key);
		log.error("[!] Repository contains : " + repository);
		log.error("---------------------------------------------------------");
	}
	
}
