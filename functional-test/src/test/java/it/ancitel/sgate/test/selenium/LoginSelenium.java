package it.ancitel.sgate.test.selenium;

import com.thoughtworks.selenium.*;
import it.ancitel.sgate.test.util.SgateSeleneseTestCase;
import it.ancitel.test.infrastruttura.interfaces.SeleniumNavigation;
import java.util.Properties;
import org.apache.log4j.Logger;


public class LoginSelenium extends SgateSeleneseTestCase implements SeleniumNavigation{
	private final static Logger logger = Logger.getLogger(LoginSelenium.class);

    @Override
	public Selenium executeNavigation(Properties prop) throws Exception {

        super.setUp(prop.getProperty("SGATE_WEB_URL"));


        selenium.open(prop.getProperty("SGATE_WEB_URL")+"/login.htm");
        selenium.type("userId", prop.getProperty("USERNAME_COMUNE"));
        selenium.type("password", prop.getProperty("PASSWORD_COMUNE"));

        selenium.click("//input[@value='Accedi']");
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