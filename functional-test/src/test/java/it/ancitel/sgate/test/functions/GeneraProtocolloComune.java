package it.ancitel.sgate.test.functions;

import it.ancitel.sgate.test.util.CodiceFiscale;
import it.ancitel.sgate.test.util.StringUtility;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import it.ancitel.test.infrastruttura.exception.RunFunctionAbnormalTermination;
import it.ancitel.test.infrastruttura.exception.TestException;
import it.ancitel.test.infrastruttura.functions.GenericRunnableFunction;
import java.sql.Connection;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;

public class GeneraProtocolloComune extends GenericRunnableFunction {

    private final static Logger log = Logger.getLogger(GeneraProtocolloComune.class);
    
	@Override
	public void execute(Properties prop, Connection conn) throws RunFunctionAbnormalTermination {


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
                ITable protocItable = it.ancitel.test.infrastruttura.util.ITableUtil.getITable(databaseConnection, "PROTOCOLLOCOMUNALE", query);
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
