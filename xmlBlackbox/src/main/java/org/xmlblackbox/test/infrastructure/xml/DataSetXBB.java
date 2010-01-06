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
