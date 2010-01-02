/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xmlblackbox.test.infrastructure.xml;


import org.dbunit.dataset.IDataSet;

/**
 *
 * @author alex
 */
public class DataSetXBB {

    private IDataSet dataset;
    private String sqlWhere;
    private String sqlIsPresent;


    public void setSqlWhere(String sql){
        this.sqlWhere = sql;
    }
    public void setDataSet(IDataSet dataaset){
        this.dataset = dataaset;
    }
    public IDataSet getDataSet(){
        return dataset;
    }
    public String getSqlWhere(){
        return sqlWhere;
    }

    /**
     * @return the sqlIsPresent
     */
    public String getSqlIsPresent() {
        return sqlIsPresent;
    }

    /**
     * @param sqlIsPresent the sqlIsPresent to set
     */
    public void setSqlIsPresent(String sqlIsPresent) {
        this.sqlIsPresent = sqlIsPresent;
    }
}
