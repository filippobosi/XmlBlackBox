package org.xmlblackbox.test.infrastructure.checks;

import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.dbunit.database.IDatabaseConnection;
import org.xmlblackbox.test.infrastructure.util.MemoryData;



public abstract class  CheckObject {

	public IDatabaseConnection conn = null;

    protected final static Logger logger = Logger.getLogger(CheckObject.class);

	public Properties testProp = null;
	public MemoryData memData = null;
	
    @Deprecated
    public Properties getTestProp() {
		return testProp;
	}
    @Deprecated
	public void setTestProp(Properties testProp) {
		this.testProp = testProp;
	}

	public abstract void initVariable() throws Exception;

    public abstract void execute(Map prop, int step) throws Exception;
     

}
