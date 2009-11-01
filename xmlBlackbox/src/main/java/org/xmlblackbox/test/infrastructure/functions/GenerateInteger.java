package org.xmlblackbox.test.infrastructure.functions;

import org.xmlblackbox.test.infrastructure.exception.RunFunctionAbnormalTermination;
import org.xmlblackbox.test.infrastructure.util.MemoryData;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GenerateInteger extends GenericRunnableFunction {



	@Override
	public List<String> getParametersName() {
		ArrayList ret = new ArrayList<String>();
		ret.add("GenInteger.min");
		ret.add("GenInteger.max");
		return ret;
	}

	@Override
	public void execute(Properties prop, MemoryData memory) throws RunFunctionAbnormalTermination{
		
	}

}
