package org.xmlblackbox.test.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.xmlblackbox.test.infrastructure.exception.RunPluginAbnormalTermination;
import org.xmlblackbox.test.infrastructure.plugin.GenericRunnablePlugin;
import org.xmlblackbox.test.infrastructure.util.MemoryData;

public class ReadNewInformationObject extends GenericRunnablePlugin  {

    private final static Logger log = Logger.getLogger(ReadNewInformationObject.class);

	@Override
	public void execute(Properties prop, MemoryData memory) throws RunPluginAbnormalTermination {

        Person person = (Person)memory.getObjectByName(prop.getProperty("nameReturnObject"));

        log.info("person.getName() "+person.getName());
        log.info("person.getSurname() "+person.getSurname());


    }

	@Override
	public List<String> getParametersName() {
		ArrayList<String> ret = new ArrayList();
		return ret;
	}

}
