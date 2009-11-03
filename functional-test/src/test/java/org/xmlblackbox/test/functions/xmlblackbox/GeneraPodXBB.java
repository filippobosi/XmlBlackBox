package org.xmlblackbox.test.functions.xmlblackbox;

import it.ancitel.sgate.test.functions.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import java.sql.Connection;
import org.apache.log4j.Logger;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.xmlblackbox.test.infrastructure.exception.RunFunctionAbnormalTermination;
import org.xmlblackbox.test.infrastructure.functions.GenericRunnableFunction;
import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.util.ITableUtil;
import org.xmlblackbox.test.infrastructure.util.MemoryData;

public class GeneraPodXBB extends GenericRunnableFunction {

    private final static Logger log = Logger.getLogger(GeneraPodXBB.class);
    
	@Override
	public void execute(Properties prop, MemoryData memory) throws RunFunctionAbnormalTermination {

        Connection conn = (Connection)memory.getObjectByName(Repository.RUN_FUNCTION);

        IDatabaseConnection databaseConnection = new DatabaseConnection(conn);
    	// init del codice fiscale
 		//Init del codice POD che deve essere univoco
		Double codicePOD =new Double((Math.random()*100000000));

        String codice_distributore = prop.getProperty("codice_distributore");
        
		log.info("codice_distributore = ("+codice_distributore+")");
    	String codice_pod= "IT"+codice_distributore+"E"+codicePOD.intValue();
    	String codicePODRandom = ""+codicePOD.intValue();

    	if (codicePODRandom.length()<8){
    		for (int i = 0; i < (8-codicePODRandom.length()); i++) {
        		codice_pod = codice_pod+"0";
			}
    	}
    	boolean protocolloOK = false;
    	while(!protocolloOK){
            try {
                log.info("codicePODStr temp = " + codice_pod);
                String query = "SELECT * FROM DOMANDA_AGEV WHERE NUM_POD= '" + codice_pod + "'";
                log.info("query " + query);
                ITable codicePODItable = ITableUtil.getITable(databaseConnection, "CODICEPOD", query);
                log.info("codicePODItable.getRowCount() " + codicePODItable.getRowCount());
                if (codicePODItable.getRowCount() == 0) {
                    protocolloOK = true;
                } else {
                    codicePOD = new Double(Math.random() * 100000000);
                    codice_pod = "IT" + codice_distributore + "E" + codicePOD.intValue();
                }
            } catch (DataSetException ex) {
                log.error("DataSetException", ex);
                throw new RunFunctionAbnormalTermination("DataSetException");
            } catch (SQLException ex) {
                log.error("SQLException", ex);
                throw new RunFunctionAbnormalTermination("SQLException");
            }
    	}

    	log.info("codice_pod finale = "+codice_pod);
    	prop.put("codice_pod", codice_pod);

	}

	@Override
	public List<String> getParametersName() {
		ArrayList<String> ret = new ArrayList();
		return ret;
	}

}
