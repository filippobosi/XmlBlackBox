/**
 *
 * This file is part of XmlBlackBox.
 *
 * XmlBlackBox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * XmlBlackBox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with XmlBlackBox.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.xmlblackbox.test.infrastructure.plugin;

import org.apache.log4j.Logger;
import org.xmlblackbox.test.infrastructure.exception.RunPluginAbnormalTermination;
import org.xmlblackbox.test.infrastructure.util.MemoryData;
import org.xmlblackbox.test.infrastructure.xml.RunPlugin;

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
	 * @return String generatedDate
	 * 
	 */
	private final static Logger log = Logger.getLogger(RunPlugin.class);

	
	@Override
	public void execute(Properties prop, MemoryData memory) throws RunPluginAbnormalTermination{
		memory.debugMemory();
		log.info("prop "+prop);
		SimpleDateFormat dateFormat = new SimpleDateFormat(prop.getProperty("pattern"));
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH,Integer.parseInt(prop.getProperty("addOrSubDays")));
		prop.put("generatedDate", dateFormat.format(cal.getTime()));
		log.info("generatedDate "+dateFormat.format(cal.getTime()));
	}

	@Override
	public List<String> getParametersName() {
		ArrayList<String> ret = new ArrayList();
//		ret.add("pattern");
//		ret.add("addOrSubDays");
		return ret;
	}
	
	

}