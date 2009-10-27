package org.xmlblackbox.test.infrastructure.xml;

import java.util.HashMap;
import java.util.Iterator;
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