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
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;

public class CaricaIdRichiestaSegnalazioneLottoDaIdDomanda extends GenericRunnableFunction {

    private final static Logger log = Logger.getLogger(CaricaIdRichiestaSegnalazioneLottoDaIdDomanda.class);
    
	@Override
	public void execute(Properties prop, Connection conn) throws RunFunctionAbnormalTermination {

        IDatabaseConnection databaseConnection = new DatabaseConnection(conn);

        log.debug("idDomanda "+prop.getProperty("idDomanda"));
        if (prop.getProperty("idDomanda")!=null){
            try{
                ITable tableRichiesta = databaseConnection.createQueryTable("RICHIESTA", "SELECT * FROM RICHIESTA_VARIAZ WHERE ID_DOMANDA_AGEV="+prop.getProperty("idDomanda"));
                if (tableRichiesta.getRowCount()>0){
                    prop.put("idRichiesta", ((BigDecimal)tableRichiesta.getValue(0, "ID")).toString());
                    log.info("idRichiesta "+((BigDecimal)tableRichiesta.getValue(0, "ID")).toString());
                }
                ITable tableSegnalazione = databaseConnection.createQueryTable("SEGNALAZIONE", "SELECT * FROM SEGNALAZIONE_SGATE WHERE ID_DOMANDA_AGEV="+prop.getProperty("idDomanda"));
                if (tableSegnalazione.getRowCount()>0){
                    prop.put("idSegnalazione", ((BigDecimal)tableSegnalazione.getValue(0, "ID")).toString());
                    log.info("idSegnalazione "+((BigDecimal)tableSegnalazione.getValue(0, "ID")).toString());
                }
                if (prop.getProperty("idRichiesta")!=null) {
                    ITable tableMovimenti = databaseConnection.createQueryTable("MOVIMENTI", "SELECT * FROM MOVIMENTI WHERE ID_RICHIESTA="+prop.getProperty("idRichiesta"));
                    if (tableMovimenti.getRowCount()>0){
                        prop.put("idLotto", ((BigDecimal)tableMovimenti.getValue(0, "ID_COMUNIC")).toString());
                        log.info("idLotto"+((BigDecimal)tableMovimenti.getValue(0, "ID_COMUNIC")).toString());
                    }
                    ITable tableSegnalazioneImpresa = databaseConnection.createQueryTable("SEGNALAZIONE_IMPRESA", "SELECT * FROM SEGNALAZIONE_IMPRESA WHERE RICHIESTAAGEVOLAZIONE_ID="+prop.getProperty("idRichiesta"));
                    if (tableSegnalazioneImpresa.getRowCount()>0){
                        prop.put("idSegnalazioneImpresa", ((BigDecimal)tableSegnalazioneImpresa.getValue(0, "ID")).toString());
                        log.info("idSegnalazioneImpresa"+((BigDecimal)tableSegnalazioneImpresa.getValue(0, "ID")).toString());
                    }
                }
            } catch (DataSetException ex) {
                log.error("DataSetException", ex);
                throw new RunFunctionAbnormalTermination("DataSetException");
            } catch (SQLException ex) {
                log.error("SQLException", ex);
                throw new RunFunctionAbnormalTermination("SQLException");
            }

        }
	}

	@Override
	public List<String> getParametersName() {
		ArrayList<String> ret = new ArrayList();
		return ret;
	}

}