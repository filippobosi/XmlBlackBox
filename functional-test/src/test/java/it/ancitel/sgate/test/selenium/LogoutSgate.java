package it.ancitel.sgate.test.selenium;

import com.thoughtworks.selenium.*;
import it.ancitel.sgate.test.util.SgateSeleneseTestCase;
import it.ancitel.test.infrastruttura.interfaces.SeleniumNavigation;
import java.util.Properties;
import org.apache.log4j.Logger;


public class LogoutSgate extends SgateSeleneseTestCase implements SeleniumNavigation{
	private final static Logger logger = Logger.getLogger(LogoutSgate.class);

    @Override
	public Selenium executeNavigation(Properties prop) throws Exception {

        super.setUp(prop.getProperty("SGATE_WEB_URL"));

		selenium.click("link=Logout");
		selenium.waitForPageToLoad("30000");
        
        return selenium;
	}

    @Override
    public void testNew() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSelenium(Selenium selenium) {
        super.selenium = selenium;
    }

    @Override
    public Selenium getSelenium() {
        return super.selenium;
    }
}