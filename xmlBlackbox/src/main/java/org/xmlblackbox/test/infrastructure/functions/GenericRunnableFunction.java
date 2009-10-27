package org.xmlblackbox.test.infrastructure.functions;

import org.xmlblackbox.test.infrastructure.exception.RunFunctionAbnormalTermination;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;



public abstract class GenericRunnableFunction implements Serializable {

	public abstract List<String> getParametersName();
	public abstract void execute(Properties prop,Connection conn) throws RunFunctionAbnormalTermination ;
	
	public void checkPrametersName(Properties prop,List parametersRequired) throws RunFunctionAbnormalTermination{
		
		if (parametersRequired == null)
			throw new RunFunctionAbnormalTermination("La funzione non può essere avviata, perchè non sono stati dichiarati i prametri necessari nell'implementazione della classe. Verifica : getParametersName() nell'implementazione della funzione.");
		
		Iterator<String> it = parametersRequired.iterator();
		while (it.hasNext()){
			String param = it.next();
			if (prop.getProperty(param) == null)
				throw new RunFunctionAbnormalTermination("La funzione non può essere avviata, non è stato dichiarato il parametro necessario : " + param);
		}
	}
}
