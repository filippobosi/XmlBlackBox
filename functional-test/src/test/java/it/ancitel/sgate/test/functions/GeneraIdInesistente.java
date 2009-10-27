/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.ancitel.sgate.test.functions;

import it.ancitel.test.infrastruttura.exception.RunFunctionAbnormalTermination;
import it.ancitel.test.infrastruttura.functions.GenericRunnableFunction;
import it.ancitel.test.infrastruttura.util.ITableUtil;
import java.math.BigDecimal;
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
public class GeneraIdInesistente extends GenericRunnableFunction {

    private final static Logger log = Logger.getLogger(CaricaIdRichiestaSegnalazioneLottoDaIdDomanda.class);

    @Override
    public void execute(Properties prop, Connection conn) throws RunFunctionAbnormalTermination {
        IDatabaseConnection databaseConnection = new DatabaseConnection(conn);

        if (prop.getProperty("idType") != null) {
            try {
                Double idDouble =new Double((Math.random()*100000));
                Integer id = idDouble.intValue();
                if (prop.getProperty("idType").equals("idRichiesta")) {
                    while (true) {
                        ITable result = ITableUtil.getITable(databaseConnection, "RICHIESTA_VARIAZ", "SELECT * FROM RICHIESTA_VARIAZ WHERE ID = " + id);
                        if (result.getRowCount() == 0) {
                            prop.put("idRichiesta", "" + id);
                            log.info("runFunction idRichiesta " + id);
                            return;
                        } else {
                            id++;
                        }
                    }
                }
                if (prop.getProperty("idType").equals("idDomanda")) {
                    
                    while (true) {
                        ITable result = ITableUtil.getITable(databaseConnection, "DOMANDA_AGEV", "SELECT * FROM DOMANDA_AGEV WHERE ID = " + id);
                        if (result.getRowCount() == 0) {
                            prop.put("idDomanda", "" + id);
                            log.info("runFunction idDomanda " + id);
                            return;
                        } else {
                            id++;
                        }
                    }
                }
                if (prop.getProperty("idType").equals("idSegnalazione")) {

                    while (true) {
                        ITable result = ITableUtil.getITable(databaseConnection, "SEGNALAZIONE_SGATE", "SELECT * FROM SEGNALAZIONE_SGATE WHERE ID = " + id);
                        if (result.getRowCount() == 0) {
                            prop.put("idSegnalazione", "" + id);
                            log.info("runFunction idSegnalazione " + id);
                            return;
                        } else {
                            id++;
                        }
                    }
                }

            } catch (DataSetException ex) {
                log.error("DataSetException", ex);
                throw new RunFunctionAbnormalTermination("DataSetException");
            } catch (SQLException ex) {
                log.error("SQLException", ex);
                throw new RunFunctionAbnormalTermination("SQLException");
            }

        } else {
            throw new RunFunctionAbnormalTermination("Manca il parametro che indica il tipo di id che si sta cercando");
        }
	}



    @Override
    public List<String> getParametersName() {
        ArrayList<String> ret = new ArrayList();
        return ret;
    }
}
