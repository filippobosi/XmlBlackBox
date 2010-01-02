

package org.xmlblackbox.test.infrastructure.xml;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.jdom.output.XMLOutputter;

import org.xmlblackbox.test.infrastructure.exception.TestException;
import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.util.ITableUtil;
import org.xmlblackbox.test.infrastructure.util.MemoryData;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.xml.XmlDataSet;
import org.xmlblackbox.test.infrastructure.exception.CheckDbException;

public class CheckDatabase extends XmlElement{

    private final static Logger logger = Logger.getLogger(CheckDatabase.class);

	private List<DataSetXBB> dataSetList = null;
	private String nome = null;
	private boolean automatico=false;
	private String tipo;
	private String database;
	private String motivo;
	private Map<String, String> parameters = new HashMap<String, String>();
    private String connection;
    private String version;

    private static final String VERSION10 = "1.0";
    private static final String VERSION11 = "1.1";


	public CheckDatabase(Element el) throws Exception {
		super(el);
		build(el);
	}

	public void build(Element dbcheckElement) throws Exception{

		CheckDatabase dbCheck=this;

    	dbCheck.setNome((String)dbcheckElement.getAttributeValue("name"));
    	dbCheck.setVersion((String)dbcheckElement.getAttributeValue("version"));
    	dbCheck.setMotivo((String)dbcheckElement.getAttributeValue("motif"));
    	dbCheck.setTipo((String)dbcheckElement.getAttributeValue("type"));
    	dbCheck.setDatabase((String)dbcheckElement.getAttributeValue("database"));
    	dbCheck.setAutomatico((String)dbcheckElement.getAttributeValue("automatic"));

        connection = dbcheckElement.getAttributeValue("connection");

        Element parametersElement = dbcheckElement.getChild("PARAMETERS");
    	if (parametersElement!=null) {
    		Iterator parametersList = parametersElement.getChildren("PARAMETER", uriXsd).iterator();
    		while (parametersList.hasNext()){
    			Element parameterElement = (Element) parametersList.next();
    			String pname = parameterElement.getAttributeValue("name");
    			String pvalue = parameterElement.getAttributeValue("value");

    			parameters.put(pname, pvalue);
    		}
    	}

    	dataSetList = readDbUnit(dbcheckElement);
		dbCheck.addDataSets(dataSetList);
//        String[] tableNames = iDataSet.getTableNames();
//		logger.debug("tableNames.length "+ tableNames.length);
//
//        ITableIterator tableIter = iDataSet.iterator();
//        logger.debug("tableIter.next() "+ tableIter.next());
//        logger.debug("tableIter.getTable().getTableMetaData().getTableName() "+ tableIter.getTable().getTableMetaData().getTableName());


	}


	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public List<DataSetXBB> getDataSetList() {
		return dataSetList;
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


	public String listaTabelle(DataSetXBB dataSet) throws DataSetException {
		String result = "";
		if ((dataSet!=null)) {
				for (int i = 0; i < dataSet.getDataSet().getTableNames().length; i++) {
					result+=dataSet.getDataSet().getTableNames()[i]+"-";
				}
		}
		return result;
	}

	public List<DataSetXBB> readDbUnit(Element dbcheckElement) throws DataSetException, IOException{

		XMLOutputter outputter = new XMLOutputter();
        List<DataSetXBB> dataSetList = new ArrayList();

        Element dataset = null;
		Iterator<Element> iter = dbcheckElement.getChildren("dataset", uriXsd).iterator();

        while(iter.hasNext()){

            DataSetXBB dataSetXbb = new DataSetXBB();
            dataset = iter.next();
            if (dataset!=null){
                dataSetXbb.setSqlWhere(dataset.getChild("table", uriXsd).getAttributeValue("where-condition"));
                logger.debug("getSqlWhere() "+dataSetXbb.getSqlWhere());
                dataSetXbb.setSqlIsPresent(dataset.getChild("table", uriXsd).getAttributeValue("is-present"));
                logger.debug("getSqlIsPresent() "+dataSetXbb.getSqlIsPresent());
            }

            logger.debug("new XMLOutputter().outputString(dataset) "+ new XMLOutputter().outputString(dataset));
            StringReader srDataset=new StringReader(new XMLOutputter().outputString(dataset));
            IDataSet expectedDataSet = null;
            logger.debug("geVersion "+getVersion());
            if (getVersion().equals(VERSION11)){
                expectedDataSet = new XmlDataSet(srDataset);
            }else if(getVersion().equals(VERSION10)){
                expectedDataSet = new FlatXmlDataSet(srDataset);
            }
            dataSetXbb.setDataSet(expectedDataSet);
            dataSetList.add(dataSetXbb);
            srDataset.close();

        }

		return dataSetList;


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

        Iterator<DataSetXBB> dataSets = getDataSetList().iterator();

        while (dataSets.hasNext()) {
            DataSetXBB dataSetXBB = dataSets.next();
            IDataSet idataSet = dataSetXBB.getDataSet();

            String[] tableNames = idataSet.getTableNames();

            logger.debug("tableNames "+ tableNames+ ". Step "+step);

            for (int k=0; k < tableNames.length; k++){
                ITable iTableAttesa = idataSet.getTable(tableNames[k]);
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



                ITable iTableReale = ITableUtil.getRealTable(conn, iTableAttesa, dataSetXBB.getSqlWhere());
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
                logger.debug("columnNameList.contains(\"is-present\")"+ columnNameList.contains("is-present"));

                if (dataSetXBB.getSqlIsPresent()!=null && iTableAttesaRipulita.getRowCount()==0){
                    throw new CheckDbException("Configuration exception. \"is-present\" attribute could not be added with no records");
                }

                if (dataSetXBB.getSqlIsPresent()!=null){
                    sqlIsPresent = new Boolean(dataSetXBB.getSqlIsPresent());
                    logger.debug("sqlIsPresent "+ sqlIsPresent);
                    if (!sqlIsPresent){
                        if (iTableReale.getRowCount()>0){
                            new CheckDbException("Resord found record non atteso nella tabella "+iTableAttesa.getTableMetaData().getTableName());
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

    /**
     * @return the connection
     */
    public String getConnection() {
        return connection;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @param sqlWhere the sqlWhere to set
     */
    public void addDataSets(List<DataSetXBB> list) {
        this.dataSetList = list;
    }

}
