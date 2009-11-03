package org.xmlblackbox.test.functions.xmlblackbox;

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
import org.xmlblackbox.test.util.StringUtility;

public class GeneraProtocolloComuneXBB extends GenericRunnableFunction {

    private final static Logger log = Logger.getLogger(GeneraProtocolloComuneXBB.class);
    
	@Override
	public void execute(Properties prop, MemoryData memory) throws RunFunctionAbnormalTermination {
        Connection conn = (Connection)memory.getObjectByName(Repository.RUN_FUNCTION);


        IDatabaseConnection databaseConnection = new DatabaseConnection(conn);
		//Init del protocollo comune che deve essere univoco
		Double protDouble =new Double((Math.random()*100000));
    	String protocolloComunale = ""+protDouble.intValue();
    	boolean protocolloOK = false;
    	while(!protocolloOK){
            try {
                log.info("protocolloComunale temp = " + protocolloComunale);
                String query = "SELECT * FROM DOMANDA_AGEV WHERE ID_PROT_COMUNALE = '" + protocolloComunale + "'";
                log.info("query " + query);
                ITable protocItable = ITableUtil.getITable(databaseConnection, "PROTOCOLLOCOMUNALE", query);
                log.info("protocItable.getRowCount() " + protocItable.getRowCount());
                if (protocItable.getRowCount() == 0) {
                    protocolloOK = true;
                } else {
                    protDouble = new Double(Math.random() * 100000);
                    protocolloComunale = "" + protDouble.intValue();
                }
            } catch (DataSetException ex) {
                log.error("DataSetException", ex);
                throw new RunFunctionAbnormalTermination("ParseException");
            } catch (SQLException ex) {
                log.error("SQLException", ex);
                throw new RunFunctionAbnormalTermination("ParseException");
            }
    	}

    	log.info("protocolloComunale finale = "+protocolloComunale);
    	prop.put("protocollo_comunale", protocolloComunale);


	}

	@Override
	public List<String> getParametersName() {
		ArrayList<String> ret = new ArrayList();
		return ret;
	}

    public String getCodiceFiscalePrefix(){

		Double prefix =new Double((Math.random()*100000));
    	while (!StringUtility.isSequenceOK(prefix.intValue())){
    		prefix =new Double((Math.random()*100000));
    	}

    	log.info("prefix.intValue() "+prefix.intValue());


    	String prefixStr = StringUtility.convertiNumeroStringa(prefix.intValue());

    	return prefixStr;
	}


}
