package org.xmlblackbox.test.infrastructure.functions;

import org.xmlblackbox.test.infrastructure.exception.RunFunctionAbnormalTermination;
import org.xmlblackbox.test.infrastructure.util.MemoryData;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class GenerateDate extends GenericRunnablePlugin {

	/**
	 * @author lele
	 * Genera una data partendo dalla data odierna secondo un pattern specificato da input usando il SimpleDataFormat 
	 * è possibile aggiungere o sottrarre giorni tramite il secondo paramtro addOrSubDays
	 * 
	 * Es: pattern = yyyy-MM-dd 
	 *     addOrSubDays = 0
	 * Return : la data di oggi nel formato anno-mese-giorno
	 * 
	 * @param String pattern,int addOrSubDays
	 * 
	 * @return String generateDate
	 * 
	 */
	
	@Override
	public void execute(Properties prop, MemoryData memory) throws RunFunctionAbnormalTermination{
		SimpleDateFormat dateFormat = new SimpleDateFormat(prop.getProperty("pattern"));
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH,Integer.parseInt(prop.getProperty("addOrSubDays")));
		prop.put("generateDate", dateFormat.format(cal.getTime()));
	}

	@Override
	public List<String> getParametersName() {
		ArrayList<String> ret = new ArrayList();
		ret.add("pattern");
		ret.add("addOrSubDays");
		return ret;
	}
	
	

}
