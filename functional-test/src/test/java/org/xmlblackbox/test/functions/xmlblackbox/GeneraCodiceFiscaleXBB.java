package org.xmlblackbox.test.functions.xmlblackbox;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import java.sql.Connection;
import org.apache.log4j.Logger;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.xmlblackbox.test.infrastructure.exception.RunFunctionAbnormalTermination;
import org.xmlblackbox.test.infrastructure.functions.GenericRunnablePlugin;
import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.util.ITableUtil;
import org.xmlblackbox.test.infrastructure.util.MemoryData;
import org.xmlblackbox.test.util.CodiceFiscale;
import org.xmlblackbox.test.util.StringUtility;

public class GeneraCodiceFiscaleXBB extends GenericRunnablePlugin {

    private final static Logger log = Logger.getLogger(GeneraCodiceFiscaleXBB.class);

	@Override
	public void execute(Properties prop, MemoryData memory) throws RunFunctionAbnormalTermination {

        Connection conn = (Connection)memory.getObjectByName(Repository.RUN_PLUGIN);

        IDatabaseConnection databaseConnection = new DatabaseConnection(conn);
        // init del codice fiscale
    	String cognomePrefixStr = getCodiceFiscalePrefix();

            	// init del codice fiscale
    	String nomePrefixStr = getCodiceFiscalePrefix();

        Calendar annoNascita = Calendar.getInstance();
        annoNascita.add(Calendar.YEAR, -30);
        String annoStr = ""+annoNascita.get(Calendar.YEAR);
        String meseStr = ""+(annoNascita.get(Calendar.MONTH)+1);
        String giornoStr = ""+annoNascita.get(Calendar.DAY_OF_MONTH);

        if (meseStr.length()==1){
            meseStr = 0+meseStr;
        }
        if (giornoStr.length()==1){
            giornoStr = 0+giornoStr;
        }

    	String codiceFiscale = null;
	    boolean codiceFiscaleOK = false;
    	while(!codiceFiscaleOK){
            try {
                log.info("nomePrefixStr temp = " + nomePrefixStr+" Nome completo "+nomePrefixStr + "Mario");
                log.info("cognomePrefixStr temp = " + cognomePrefixStr+" Cognome completo "+cognomePrefixStr + "Rossi");
                log.info("dataNascita temp = " + annoStr + meseStr + giornoStr);
                codiceFiscale = CodiceFiscale.getCF(nomePrefixStr + "Mario", cognomePrefixStr + "Rossi", "M", annoStr + meseStr + giornoStr, "G478");
                log.info("codiceFiscale temp = " + codiceFiscale);
                String query = "SELECT * FROM PERSONA WHERE COD_FIS= '" + codiceFiscale + "'";
                log.info("query " + query);
                ITable codiceFiscaleItable = ITableUtil.getITable(databaseConnection, "CODICEFISCALE", query);
                log.info("codiceFiscaleItable.getRowCount() " + codiceFiscaleItable.getRowCount());
                if (codiceFiscaleItable.getRowCount() == 0) {
                    codiceFiscaleOK = true;
                } else {
                    cognomePrefixStr = getCodiceFiscalePrefix();
                }
            } catch (DataSetException ex) {
                log.error("DataSetException", ex);
                throw new RunFunctionAbnormalTermination("DataSetException");
            } catch (SQLException ex) {
                log.error("SQLException", ex);
                throw new RunFunctionAbnormalTermination("SQLException");
            } catch (ParseException ex) {
                log.error("ParseException", ex);
                throw new RunFunctionAbnormalTermination("ParseException");
            }
    	}

    	log.info("codiceFiscale finale = "+codiceFiscale);
    	prop.put("codice_fiscale", codiceFiscale);
    	prop.put("nome", nomePrefixStr+"Mario");
    	prop.put("cognome", cognomePrefixStr+"Rossi");
    	prop.put("data_nascita_giorno", giornoStr);
    	prop.put("data_nascita_mese", meseStr);
    	prop.put("data_nascita_anno", annoStr);

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
