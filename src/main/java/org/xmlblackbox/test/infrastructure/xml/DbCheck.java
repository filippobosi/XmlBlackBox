

package org.xmlblackbox.test.infrastructure.xml;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.dbunit.Assertion;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import org.xmlblackbox.test.infrastructure.exception.TestException;
import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.replacement.ReplaceFromXML;
import org.xmlblackbox.test.infrastructure.util.ITableUtil;
import org.xmlblackbox.test.infrastructure.util.MemoryData;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class DbCheck extends XmlElement{

    private final static Logger logger = Logger.getLogger(DbCheck.class);

	private IDataSet dataSet = null;
	private String nome = null;
	private boolean automatico=false;
	private String tipo;
	private String database;
	private String motivo;
	private List listaInsert = new Vector();
	private Map<String, String> parameters = new HashMap<String, String>();

	public DbCheck(Element el) throws Exception {
		super(el);
		build(el);
	}
	
	public void build(Element dbcheckElement) throws Exception{
		
		DbCheck dbCheck=this;
		
    	dbCheck.setNome((String)dbcheckElement.getAttributeValue("nome"));
    	dbCheck.setMotivo((String)dbcheckElement.getAttributeValue("motivo"));    	
    	dbCheck.setTipo((String)dbcheckElement.getAttributeValue("tipo"));
    	dbCheck.setDatabase((String)dbcheckElement.getAttributeValue("database"));
    	dbCheck.setAutomatico((String)dbcheckElement.getAttributeValue("automatico"));

        Element parametersElement = dbcheckElement.getChild("PARAMETERS");
    	if (parametersElement!=null) {
    		Iterator parametersList = parametersElement.getChildren("PARAMETER").iterator();
    		while (parametersList.hasNext()){
    			Element parameterElement = (Element) parametersList.next();
    			String pname = parameterElement.getAttributeValue("name");
    			String pvalue = parameterElement.getAttributeValue("value");

    			parameters.put(pname, pvalue);
    		}
    	}


    	List listaInsert = dbcheckElement.getChildren("INSERT_UPDATE_QUERY");
    	Iterator iter = listaInsert.iterator();
    	List vector = new Vector();
    	int index = 0;
    	while(iter.hasNext()){
    		vector.add(((Element)iter.next()).getAttributeValue("value"));
    		index++;
    	}
    	dbCheck.setListaInsert(vector);
    	
    	IDataSet iDataSet = readDbUnit(dbcheckElement);
		dbCheck.setDBUnitDataSet(iDataSet);

	}
	
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setDBUnitDataSet(IDataSet dataSet) {
		this.dataSet = dataSet;
	}

	public IDataSet getDBUnitDataSet() {
		return dataSet;
	}

	public void setListaInsert(List listaInsert) {
		this.listaInsert = listaInsert;
	}

	public List getListaInsert() {
		return listaInsert;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
	
	public String listaTabelle() throws DataSetException {
		String result = "";
		if ((getDBUnitDataSet()!=null)) {
				for (int i = 0; i < dataSet.getTableNames().length; i++) {
					result+=dataSet.getTableNames()[i]+"-";
				}
		}
		return result;
	}

	public IDataSet readDbUnit(Element dbcheckElement) throws DataSetException, IOException{
		
		XMLOutputter outputter = new XMLOutputter();
//		File file = new File(Thread.currentThread().getName()+"dbunit.txt");
//		file.createNewFile();
		Element dataset = null;
		dataset = dbcheckElement.getChild("dataset");
		
		//Nel caso di DATASET_DBUNIT con solo le query di inserimento dati in questo modo non da errore
		if (dataset==null){
			dataset = new Element("dataset");
		}
		
		StringReader srDataset=new StringReader(new XMLOutputter().outputString(dataset));
		
		IDataSet expectedDataSet = new FlatXmlDataSet(srDataset);
		srDataset.close();
		
//		inputStreamReader.close();
		
		return expectedDataSet;
		
	
	}

	public boolean isAutomatico() {
		return automatico;
	}

	public void setAutomatico(boolean automatico) {
		this.automatico = automatico;
	}

	public void setAutomatico(String automatico) {
		this.automatico = "TRUE".equalsIgnoreCase(automatico);
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	@Override
	public String getRepositoryName() {
		return Repository.DB_CHECK;
	}
	
    public void checkDB(MemoryData memoryData, IDatabaseConnection conn, int step) throws Exception, TestException {
		String[] tableNames = getDBUnitDataSet().getTableNames();
		
		logger.debug("tableNames "+ tableNames+ ". Step "+step);
		
		for (int k=0; k < tableNames.length; k++){
			ITable iTableAttesa = getDBUnitDataSet().getTable(tableNames[k]);
			logger.debug("iTableAttesa.getTableMetaData() "+ iTableAttesa.getTableMetaData().getTableName());

			Column[] column = iTableAttesa.getTableMetaData().getColumns();
			for (int i = 0; i < column.length; i++) {
				logger.info("iTableAttesa column["+i+"] "+column[i].getColumnName());
			}

			List<Column> columnList = Arrays.asList(column);  
			List<String> columnNameList = new ArrayList<String>();  
			for (int i = 0; i < columnList.size(); i++) {
				columnNameList.add(columnList.get(i).getColumnName());
			} 
			
			
			
			ITable iTableReale = ITableUtil.getRealTable(conn, iTableAttesa);
			ITable iTableAttesaRipulita = ITableUtil.removeITableFields(iTableAttesa);


			Column[] columnTableReale = iTableReale.getTableMetaData().getColumns();
			for (int i = 0; i < columnTableReale.length; i++) {
				logger.info("columnTableReale column["+i+"] "+columnTableReale[i].getColumnName());
			}
			Column[] columnTableAttesaRipulita = iTableAttesaRipulita.getTableMetaData().getColumns();
			for (int i = 0; i < columnTableAttesaRipulita.length; i++) {
				logger.info("columnTableAttesaRipulita column["+i+"] "+columnTableAttesaRipulita[i].getColumnName());
			}

			logger.debug("iTableReale.getRowCount() "+ iTableReale.getRowCount());
			logger.debug("iTableAttesaRipulita.getRowCount() "+ iTableAttesaRipulita.getRowCount());

			boolean sqlIsPresent = false;
			logger.debug("columnNameList.contains(\"SQL.ISPRESENT\")"+ columnNameList.contains("SQL.ISPRESENT"));
			
			if (columnNameList.contains("SQL.ISPRESENT")){
				sqlIsPresent = new Boolean((String)iTableAttesa.getValue(0,"SQL.ISPRESENT"));
				logger.debug("sqlIsPresent "+ sqlIsPresent);
				if (!sqlIsPresent){
					if (iTableReale.getRowCount()>0){
						new TestException("Resord found record non atteso nella tabella "+iTableAttesa.getTableMetaData().getTableName());
					}
				}else{
    				logger.debug("Execution verify for dbCheck "+ getNome()+ ". Step "+step);
                    Assertion.assertEquals(iTableAttesaRipulita, iTableReale);
				}

			}else{
			
				Column[] colonne = iTableReale.getTableMetaData().getColumns();
				for (int i = 0; i < colonne.length; i++) {
					if (iTableReale.getRowCount()>0)
						logger.debug("iTableReale.getValue(0, iTableReale["+i+"]) "+iTableReale.getValue(0, colonne[i].getColumnName()));
				}

                logger.debug("Execution verify for dbCheck "+ getNome()+ ". Step "+step);
                Assertion.assertEquals(iTableAttesaRipulita, iTableReale);
			}			
			logger.debug("Tables name"+ tableNames[k]);
		}
	}

	
}

/*
 * $Id: $
 * $Log: $
 */