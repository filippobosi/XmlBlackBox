package it.ancitel.sgate.test.util;

import java.io.File;

import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;
import org.apache.log4j.Logger;

public abstract class SgateSeleneseTestCase extends SeleneseTestCase {

    private final static Logger logger = Logger.getLogger(SgateSeleneseTestCase.class);

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

	public void setUp(String url) {

        logger.info("firstSelenium "+firstSelenium);
		if (firstSelenium == null) {
			startServer();
			selenium = new DefaultSelenium("localhost", 4545, "*chrome", url);
			selenium.start();
			firstSelenium = selenium;
		}
		else {
			selenium = firstSelenium;
		}
	}

	private void startServer() {
		RemoteControlConfiguration remote = new RemoteControlConfiguration();
		remote.setPort(4545); 
        remote.setProfilesLocation(new File("src/test/resources/firefox/"));
        remote.setFirefoxProfileTemplate(new File("src/test/resources/firefox/"));

		try {

			server = new SeleniumServer(remote);

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
