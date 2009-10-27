/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.ancitel.sgate.test.functions;

import it.ancitel.test.infrastruttura.exception.RunFunctionAbnormalTermination;
import it.ancitel.test.infrastruttura.functions.GenericRunnableFunction;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import java.util.List;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;

/**
 *
 * @author kewell
 */
public class AbilitaCafPerComune extends GenericRunnableFunction {

    private final static Logger log = Logger.getLogger(CaricaIdRichiestaSegnalazioneLottoDaIdDomanda.class);

    @Override
    public void execute(Properties prop, Connection conn) throws RunFunctionAbnormalTermination {
        IDatabaseConnection databaseConnection = new DatabaseConnection(conn);
        log.info("Function AbilitaCafPerComune");
        try {
            ITable tableUtente = databaseConnection.createQueryTable("UTENTE", "SELECT * FROM UTENTE WHERE USERID='" + prop.getProperty("caf") + "'");
            String admin = (String) tableUtente.getValue(0, "ID_UTENTE_RIF");
            log.info("Utente Admin " + admin);
            int i = conn.createStatement().executeUpdate("UPDATE COMUNI_CAF_PROVINCIALE SET STATO_DOMANDA='presentata', ABILITATO=1 WHERE COMUNE='" + prop.getProperty("idComune") + "' AND AGENZIA_CAF_PROVINCIALE='" + admin + "'");
            log.info("I record modificati sono " + i);
        } catch (DataSetException ex) {
            java.util.logging.Logger.getLogger(AbilitaCafPerComune.class.getName()).log(Level.SEVERE, null, ex);
            log.error("DataSetException", ex);
            throw new RunFunctionAbnormalTermination("Errore nell'abilitazione del caf");
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(AbilitaCafPerComune.class.getName()).log(Level.SEVERE, "Errore nell'abilitazione del caf", ex);
            log.error("SqlException", ex);
            throw new RunFunctionAbnormalTermination("Errore nell'abilitazione del caf");
        }

    }

    @Override
    public List<String> getParametersName() {
        ArrayList<String> ret = new ArrayList();
        return ret;
    }
}
