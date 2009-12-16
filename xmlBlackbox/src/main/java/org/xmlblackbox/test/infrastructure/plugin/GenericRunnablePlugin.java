package org.xmlblackbox.test.infrastructure.plugin;

import org.xmlblackbox.test.infrastructure.exception.RunPluginAbnormalTermination;
import org.xmlblackbox.test.infrastructure.util.MemoryData;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;



public abstract class GenericRunnablePlugin implements Serializable {

	public abstract List<String> getParametersName();
	public abstract void execute(Properties prop,MemoryData memory) throws RunPluginAbnormalTermination ;
	
	public void checkPrametersName(Properties prop,List parametersRequired) throws RunPluginAbnormalTermination{
		
		if (parametersRequired == null)
			throw new RunPluginAbnormalTermination("La funzione non puo' essere avviata, perche' non sono stati dichiarati i prametri necessari nell'implementazione della classe. Verifica : getParametersName() nell'implementazione della funzione.");
		
		Iterator<String> it = parametersRequired.iterator();
		while (it.hasNext()){
			String param = it.next();
			if (prop.getProperty(param) == null)
				throw new RunPluginAbnormalTermination("La funzione non puo' essere avviata, non e' stato dichiarato il parametro necessario : " + param);
		}
	}
}
