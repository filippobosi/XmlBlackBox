/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.ancitel.sgate.test.functions;

import it.ancitel.test.infrastruttura.exception.RunFunctionAbnormalTermination;
import it.ancitel.test.infrastruttura.functions.GenericRunnableFunction;
import it.ancitel.test.infrastruttura.util.ITableUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;

/**
 *
 * @author kewell
 */
public class GeneraProtocolloCaf extends GenericRunnableFunction {

    private final static Logger log = Logger.getLogger(GeneraIsee.class);

	@Override
	public void execute(Properties prop, Connection conn) throws RunFunctionAbnormalTermination {

        //Init del protocollo caf per l'invio di una domanda via web-file che deve essere univoco
        IDatabaseConnection databaseConnection = new DatabaseConnection(conn);
		Double protDouble =new Double((Math.random()*100000));
    	String protocolloCaf = ""+protDouble.intValue();
    	boolean protocolloOK = false;
    	while(!protocolloOK){
    		log.info("protocolloCaf temp = "+protocolloCaf);
    		String query = "SELECT * FROM DOMANDA_AGEV WHERE PROT_PROV = '"+protocolloCaf+"'";
    		log.info("query "+query);
    		ITable protocItable;
            try {
                protocItable = ITableUtil.getITable(databaseConnection, "PROTOCOLLOCAF", query);
            } catch (DataSetException ex) {
                log.error("DataSetException", ex);
                throw new RunFunctionAbnormalTermination("ParseException");
            } catch (SQLException ex) {
                log.error("SQLException", ex);
                throw new RunFunctionAbnormalTermination("ParseException");
            }

    		log.info("protocItable.getRowCount() "+protocItable.getRowCount());
    		if (protocItable.getRowCount()==0){
    			protocolloOK = true;
    		}else{
    			protDouble =new Double((Math.random()*100000));
    			protocolloCaf = ""+protDouble.intValue();
    		}
    	}

    	log.info("protocolloCaf finale = "+protocolloCaf);
    	prop.put("protocolloCaf", protocolloCaf);
    }

	@Override
	public List<String> getParametersName() {
		ArrayList<String> ret = new ArrayList();
		return ret;
	}

}
