package it.ancitel.sgate.test.functions.xmlblackbox;

import it.ancitel.sgate.test.functions.*;
import java.sql.SQLException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import java.sql.Connection;

import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.xmlblackbox.test.infrastructure.exception.RunFunctionAbnormalTermination;
import org.xmlblackbox.test.infrastructure.functions.GenericRunnableFunction;

public class GeneraIseeXBB extends GenericRunnableFunction {

    private final static Logger log = Logger.getLogger(GeneraIseeXBB.class);

	@Override
	public void execute(Properties prop, Connection conn) throws RunFunctionAbnormalTermination {

        IDatabaseConnection databaseConnection = new DatabaseConnection(conn);
		//Init del codice POD che deve essere univoco
		Double codiceISEE=new Double((Math.random()*100000));
    	String codice_isee= "ISEETEST"+codiceISEE.intValue();
    	boolean protocolloOK = false;
    	while(!protocolloOK){
    		log.info("codice_isee temp = "+codice_isee);
    		String query = "SELECT * FROM ATTESTAZIONE_ISEE WHERE COD_ISEE= '"+codice_isee+"'";
    		log.info("query "+query);
    		ITable codicePODItable;
            try {
                codicePODItable = it.ancitel.test.infrastruttura.util.ITableUtil.getITable(databaseConnection, "CODICEISEE", query);
            } catch (DataSetException ex) {
                log.error("DataSetException", ex);
                throw new RunFunctionAbnormalTermination("DataSetException");
            } catch (SQLException ex) {
                log.error("SQLException", ex);
                throw new RunFunctionAbnormalTermination("SQLException");
            }


    		log.info("codicePODItable.getRowCount() "+codicePODItable.getRowCount());
    		if (codicePODItable.getRowCount()==0){
    			protocolloOK = true;
    		}else{
    			codiceISEE =new Double((Math.random()*100000));
    			codice_isee = ""+codiceISEE.intValue();
    		}
    	}

    	log.info("codice_isee finale = "+codice_isee);
    	prop.put("codice_isee", codice_isee);
    }
    
	@Override
	public List<String> getParametersName() {
		ArrayList<String> ret = new ArrayList();
		return ret;
	}

}
