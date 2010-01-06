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
