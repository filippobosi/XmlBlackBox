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

public interface Replacer {
	
	public static final String MAIN_SOURCE = "MAIN"; 
	
	public abstract String replace(String text) throws Exception;

	public abstract String getReplacementValue(Object source, String sourceName, String value) throws Exception;

	public abstract String getEndDelimiter();

	public abstract void setEndDelimiter(String endDelimiter);

	public abstract String getStartDelimiter();

	public abstract void setStartDelimiter(String startDelimiter);

	public abstract Object getSource();

	public abstract void setSource(Object source);

}