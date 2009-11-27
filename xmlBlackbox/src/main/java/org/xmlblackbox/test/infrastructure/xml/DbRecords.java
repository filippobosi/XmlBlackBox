
package org.xmlblackbox.test.infrastructure.xml;

import org.xmlblackbox.test.infrastructure.exception.TestException;
import java.io.IOException;

import java.sql.SQLException;
import org.apache.xmlbeans.XmlException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.jdom.Element;
import org.apache.log4j.Logger;


import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import org.apache.xmlbeans.XmlObject;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.ITable;
import org.xmlblackbox.test.infrastructure.util.MemoryData;

/**
 *
 * @author Crea
 *
 */
public class DbRecords extends XmlElement{


    private final static Logger log = Logger.getLogger(DbRecords.class);

    private List  recordList = new Vector();

	public DbRecords(Element dbRecordElement) throws Exception {
		super(dbRecordElement);
		build(dbRecordElement);
	}
/*

        <DB-RECORD>
                <RECORD columnid="ID">
                    <COLUMN name="ID" value="">
                    <COLUMN name="DATA" value="">
                </RECORD>
                <RECORD sqlwhere="ID=3 AND TYPE='S'">
                    <COLUMN name="ID" value="">
                    <COLUMN name="TYPE" value="">
                    <COLUMN name="DATA" value="">
                </RECORD>
        </DB-RECORD>
 */
	public void build(Element dbRecordElement) throws Exception {
            log.info("dbRecordElement "+dbRecordElement);
            log.info("dbRecordElement.getAttributeValue(name) "+dbRecordElement.getAttributeValue("name"));
            recordList = new Vector();
            Iterator<Element> recordIterator = dbRecordElement.getChildren("RECORD").iterator();
            
            while(recordIterator.hasNext()){
                Record record = new Record();
                Element recordVariable = recordIterator.next();
                record.setColumnId(recordVariable.getAttributeValue("columnid"));
                record.setWhereClause(recordVariable.getAttributeValue("sqlwhere"));
                Iterator<Element> columnIterator = recordVariable.getChildren("COLUMN").iterator();
                while(columnIterator.hasNext()){
                    Element columnVariable = columnIterator.next();
                    if (columnVariable!=null){
                        record.setColumn(columnVariable.getAttributeValue("name"),
                                recordVariable.getAttributeValue("value"));
                        getRecordList().add(record);
                    }
                }
            }
	}
	
	@Override
	public String getRepositoryName() {
		return Repository.DB_CHECK;
	}

    /**
     * @return the setList
     */
    public List getRecordList() {
        return recordList;
    }

    /**
     * @param setList the setList to set
     */
    public void setRecordList(List recordList) {
        this.recordList = recordList;
    }


}