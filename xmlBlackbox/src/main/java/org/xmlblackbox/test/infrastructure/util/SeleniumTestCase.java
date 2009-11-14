package org.xmlblackbox.test.infrastructure.util;


import java.io.File;
import java.util.Properties;

import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;
import org.apache.log4j.Logger;

public abstract class SeleniumTestCase extends SeleneseTestCase {

    private final static Logger logger = Logger.getLogger(SeleniumTestCase.class);

	public abstract void testNew() throws Exception;

	private static Selenium firstSelenium;
	private String userID = "";
	private String password = "";
	private static SeleniumServer server = null;

	protected String getUserID() {
		return this.userID;
	}

	protected String getPassword() {
		return this.password;
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		//stopServer();
	}

	private void stopServer() {
		try {
			server.stop();
		}
		catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	public void setUp(String url, Properties prop) {

        logger.debug("url  "+url);
        logger.debug("prop.getProperty(SELENIUM_HOST_SERVER) "+prop.getProperty("SELENIUM_HOST_SERVER"));
        logger.debug("prop.getProperty(SELENIUM_PORT_SERVER) "+prop.getProperty("SELENIUM_PORT_SERVER"));

        
        String server_ip = prop.getProperty("SELENIUM_HOST_SERVER");
        Integer server_port = new Integer(prop.getProperty("SELENIUM_PORT_SERVER"));
        
        
        logger.info("firstSelenium "+firstSelenium);
		if (firstSelenium == null) {
			startServer(prop);
			selenium = new DefaultSelenium(server_ip, server_port, "*chrome", url);
	        logger.info("selenium "+selenium);
			try{
				selenium.start();
			}catch(Exception e){
				logger.error("Exception ", e);
			}
			firstSelenium = selenium;
		}
		else {
			selenium = firstSelenium;
		}
	}

	private void startServer(Properties prop) {
        logger.info("prop.getProperty(SELENIUM_HOST_SERVER) "+prop.getProperty("SELENIUM_HOST_SERVER"));
        logger.info("prop.getProperty(SELENIUM_PORT_SERVER) "+prop.getProperty("SELENIUM_PORT_SERVER"));
        
        RemoteControlConfiguration remote = new RemoteControlConfiguration();
		remote.setPort(new Integer(prop.getProperty("SELENIUM_PORT_SERVER"))); 
        remote.setProfilesLocation(new File("src/test/resources/firefox/"));
        remote.setFirefoxProfileTemplate(new File("src/test/resources/firefox/"));

		try {

			server = new SeleniumServer(true, remote);
			

		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

			server.start();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

    public void setSelenium(Selenium selenium){
        firstSelenium = selenium;
    }

}
