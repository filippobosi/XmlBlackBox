/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xmlblackbox.test.infrastructure.xml;


/**
 *
 * @author examar
 */
public class XmlInsertRemoveNodeRow extends XmlCheckRow implements XmlRowInterface{


    public static String TYPE_INSERT="INSERT_NODE";
    public static String TYPE_REMOVE="INSERT_REMOVE";

    private String nome;
	private String value;
    private String fileName;
    private String namespace;
    private String xPath;
    
    private String type;
    private String outputFileName;


    public XmlInsertRemoveNodeRow(){
        
    }
    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }


    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the namespace
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * @param namespace the namespace to set
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * @return the xPath
     */
    public String getXPath() {
        return xPath;
    }

    /**
     * @param xPath the xPath to set
     */
    public void setXPath(String xPath) {
        this.xPath = xPath;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the outputFileName
     */
    public String getOutputFileName() {
        return outputFileName;
    }

    /**
     * @param outputFileName the outputFileName to set
     */
    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

}
