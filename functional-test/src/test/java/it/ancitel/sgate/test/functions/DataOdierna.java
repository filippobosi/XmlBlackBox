/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.ancitel.sgate.test.functions;

import it.ancitel.test.infrastruttura.exception.RunFunctionAbnormalTermination;
import it.ancitel.test.infrastruttura.functions.GenericRunnableFunction;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author kewell
 */
public class DataOdierna extends GenericRunnableFunction {

    private final static Logger log = Logger.getLogger(GeneraIsee.class);

	@Override
	public void execute(Properties prop, Connection conn) throws RunFunctionAbnormalTermination {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        prop.put("dataOdierna", dateFormat.format(new Date()));
        log.info("dataOdierna finale = "+dateFormat.format(new Date()));
    }

	@Override
	public List<String> getParametersName() {
		ArrayList<String> ret = new ArrayList();
		return ret;
	}

}
