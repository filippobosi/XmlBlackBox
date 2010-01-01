package org.xmlblackbox.test.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.xmlblackbox.test.infrastructure.exception.RunPluginAbnormalTermination;
import org.xmlblackbox.test.infrastructure.plugin.GenericRunnablePlugin;
import org.xmlblackbox.test.infrastructure.util.MemoryData;

public class CreateNewInformationObject extends GenericRunnablePlugin  {

    private final static Logger log = Logger.getLogger(CreateNewInformationObject.class);

	@Override
	public void execute(Properties prop, MemoryData memory) throws RunPluginAbnormalTermination {


        Person person = new Person();
        person.setName("Lorenz");
        person.setSurname("Manzon");

        memory.setObject(prop.getProperty("nameReturnObject"), person);
        






    }

	@Override
	public List<String> getParametersName() {
		ArrayList<String> ret = new ArrayList();
		return ret;
	}

}
