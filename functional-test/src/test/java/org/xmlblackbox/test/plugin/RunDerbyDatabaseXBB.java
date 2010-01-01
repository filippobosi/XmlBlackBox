package org.xmlblackbox.test.plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.xmlblackbox.test.infrastructure.exception.RunPluginAbnormalTermination;
import org.xmlblackbox.test.infrastructure.plugin.GenericRunnablePlugin;
import org.xmlblackbox.test.infrastructure.util.MemoryData;

public class RunDerbyDatabaseXBB extends GenericRunnablePlugin  {

    private final static Logger log = Logger.getLogger(RunDerbyDatabaseXBB.class);

	@Override
	public void execute(Properties prop, MemoryData memory) throws RunPluginAbnormalTermination {

		String driver = (String)prop.get("driver");
		log.info("driver "+driver);
        try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            log.info("Loaded the appropriate driver");

			Properties props = new Properties(); // connection properties
            props.put("user", "xmlblackbox");
            props.put("password", "xmlblackbox");

            Connection conn = DriverManager.getConnection("jdbc:derby:xmlblackbox;create=true", props);

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RunPluginAbnormalTermination();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RunPluginAbnormalTermination();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RunPluginAbnormalTermination();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RunPluginAbnormalTermination();
		}

    }

	@Override
	public List<String> getParametersName() {
		ArrayList<String> ret = new ArrayList();
		return ret;
	}

}
