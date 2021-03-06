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

import java.util.List;
import java.util.Vector;

import org.jdom.Element;

public abstract class Runnable extends XmlElement implements XmlCheckList {

	protected String nome = null;
	private String fileInput = null;
	private String fileOutput = null;
	private List listaTemplate = null;
	private List xml_check = new Vector();
	
		
	public Runnable(Element el) {
		super(el);
	}

	public void setFileInput(String fileInput) {
		this.fileInput = fileInput;
	}

	public String getFileInput() {
		return fileInput;
	}

	public void setFileOutput(String fileOutput) {
		this.fileOutput = fileOutput;
	}

	public String getFileOutput() {
		return fileOutput;
	}

	public void setFileTemplate(List listaTemplate) {
		this.listaTemplate = listaTemplate;
	}

//	public List getFileTemplate() {
//		return listaTemplate;
//	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public List getListaCheck() {
		return xml_check;
	}

	public void setListaCheck(List listaCheck) {
		this.xml_check = listaCheck;
	}
}