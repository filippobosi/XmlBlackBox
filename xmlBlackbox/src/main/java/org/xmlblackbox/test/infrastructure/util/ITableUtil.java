package org.xmlblackbox.test.infrastructure.util;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementTable;



public class ITableUtil{
	
	private IDatabaseConnection conn = null;
	protected final static Logger log = Logger.getLogger(ITableUtil.class);
	private static String SQL_ISPRESENT= "SQL.ISPRESENT";
	private static String SQL_WHERE = "SQL.WHERE";
	
	
	public static ITable removeITableFields(ITable tabella) throws DataSetException{
		boolean isSQLWhere = isSetITable(tabella,"SQL.WHERE");
		boolean isSQLIsPresent= isSetITable(tabella,"SQL.ISPRESENT");
			
		if (!isSQLWhere && !isSQLIsPresent){
			return tabella;
		}
		
		Column[] colonneOriginali = tabella.getTableMetaData().getColumns();
		List<Column> columnList = Arrays.asList(colonneOriginali); 
		List<String> columnNameList = new ArrayList<String>();  
		for (int i = 0; i < columnList.size(); i++) {
			columnNameList.add(columnList.get(i).getColumnName());
		} 

		List<String> values = new ArrayList<String>();
		List<Column> colonnePulite= new ArrayList<Column>();
		
		for (int i = 0; i < columnNameList.size(); i++) {
			if (!columnNameList.get(i).equals(SQL_ISPRESENT) && !columnNameList.get(i).equals(SQL_WHERE)){
				log.info("Aggiunta la colonna alla nuova tabella "+columnList.get(i).getColumnName());
				colonnePulite.add(columnList.get(i));
				values.add((String)tabella.getValue(0,columnList.get(i).getColumnName()));
			}
		}
				
		DefaultTable defaultTable = new DefaultTable(tabella.getTableMetaData().getTableName(), colonnePulite.toArray(new Column[colonnePulite.size()]));
		
		defaultTable.addRow((String[])values.toArray(new String[values.size()]));
		
		Column[] column = defaultTable.getTableMetaData().getColumns();
		for (int i = 0; i < column.length; i++) {
			log.info("defaultTable column["+i+"] "+column[i].getColumnName());
			log.info("defaultTable value["+i+"] "+ (String)defaultTable.getValue(0,column[i].getColumnName()));
		}
		return defaultTable;
		
		
		
	}
	

	public static ITable getRealTable(IDatabaseConnection conn, ITable expectedTable, String sqlWhere) throws DataSetException, SQLException{
		
		String sql = null;
		
		log.debug("isSetITable(expectedTable,SQL.WHERE) "+isSetITable(expectedTable,"SQL.WHERE"));
		log.debug("isSetITable(expectedTable,SQL.ISPRESENT) "+isSetITable(expectedTable,"SQL.ISPRESENT"));

        String columnNames = "";
        Column[] column = expectedTable.getTableMetaData().getColumns();
        //Index replace i that could be not correct if a field is filled with SQL.PRESENT or SQL.WHERE
        int index = 0;
        for (int i = 0; i < column.length; i++) {
            if (!column[index].getColumnName().equals(SQL_ISPRESENT) && !column[index].getColumnName().equals(SQL_WHERE)){
                if (i==0){
                    columnNames += column[index].getColumnName();
                }else{
                    columnNames += ", "+column[index].getColumnName();
                }
                index++;
            }
        }
        log.debug("columnNames "+columnNames);
        log.debug("sqlWhere "+sqlWhere);

		if (true){
//			sql="SELECT "+columnNames+" FROM "+expectedTable.getTableMetaData().getTableName()
//				+" WHERE " + expectedTable.getValue(0,"SQL.WHERE");
//			log.debug("SQL.WHERE sql "+sqlWhere);
			sql="SELECT "+columnNames+" FROM "+expectedTable.getTableMetaData().getTableName()
				+" WHERE " + sqlWhere;
			log.debug("SQL.WHERE sql "+sqlWhere);
		}else{
			
			
			/** 
			 * In mancanza della opzione SQL.WHERE viene preso il primo campo come chiave
			 */

			log.debug("expectedTable.getTableMetaData().getColumns()[0].getSqlTypeName() "+expectedTable.getTableMetaData().getColumns()[0].getSqlTypeName());
			log.debug("expectedTable.getTableMetaData().getColumns()[0].getSqlTypeName()getDataType().toString() "+expectedTable.getTableMetaData().getColumns()[0].getDataType().toString());

            sql="SELECT "+columnNames+" FROM "+expectedTable.getTableMetaData().getTableName()
				+" WHERE " +expectedTable.getTableMetaData().getColumns()[0].getColumnName()
					+"='"+expectedTable.getValue(0, expectedTable.getTableMetaData().getColumns()[0].getColumnName()) +"'";
			log.debug("NO SQL.WHERE sql "+sql);
		}
		
		log.debug("expectedTable.getTableMetaData().getTableName() "+expectedTable.getTableMetaData().getTableName());
		log.debug("sql "+sql);
		log.debug("conn "+conn);
		
    	ITable actualData = conn.createQueryTable(expectedTable.getTableMetaData().getTableName(), sql);
    	
		
		return actualData;
	}

    public static boolean isSetITable(ITable expectedTable,String columnName) throws DataSetException{
    	Column campi[] = expectedTable.getTableMetaData().getColumns();

    	for (int i = 0; i < campi.length; i++) {
			if(campi[i].getColumnName().equalsIgnoreCase(columnName))
				return true;
		}

    	return false;
    }
    
    public static ITable getITable(IDatabaseConnection conn, String name, String sql) throws DataSetException, SQLException{
    	ITable actualData = conn.createQueryTable(name, sql);
		return actualData;
    	
    }

}
