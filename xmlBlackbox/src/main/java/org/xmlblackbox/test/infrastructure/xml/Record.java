/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xmlblackbox.test.infrastructure.xml;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author examar
 */
public class Record {

    private String whereClause;
    private String columnId;
    private HashMap<String, String> columnList;
    private String connection;


    public Record(){
        
    }

    /**
     * @return the connection
     */
    public String getConnection() {
        return connection;
    }

    /**
     * @param connection the connection to set
     */
    public void setConnection(String connection) {
        this.connection = connection;
    }

    /**
     * @return the whereClause
     */
    public String getWhereClause() {
        return whereClause;
    }

    /**
     * @param whereClause the whereClause to set
     */
    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }

    /**
     * @return the column
     */
    public HashMap<String, String> getColumnList() {
        return columnList;
    }

    /**
     * @param column the column to set
     */
    public void setColumnList(HashMap<String, String> columnList) {
        this.columnList = columnList;
    }

    /**
     * @return the column
     */
    public String getColumn(String column) {
        return columnList.get(column);
    }

    /**
     * @param column the column to set
     */
    public void setColumn(String name, String value) {
        this.columnList.put(name, value);
    }

    /**
     * @return the columnId
     */
    public String getColumnId() {
        return columnId;
    }

    /**
     * @param columnId the columnId to set
     */
    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }


}
