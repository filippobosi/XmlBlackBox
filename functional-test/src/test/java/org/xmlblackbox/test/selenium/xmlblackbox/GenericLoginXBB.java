package org.xmlblackbox.test.selenium.xmlblackbox;

import com.thoughtworks.selenium.*;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.interfaces.SeleniumNavigation;
import org.xmlblackbox.test.infrastructure.util.MemoryData;
import org.xmlblackbox.test.util.SgateSeleneseTestCase;


public class GenericLoginXBB extends SgateSeleneseTestCase implements SeleniumNavigation{
	private final static Logger logger = Logger.getLogger(GenericLoginXBB.class);

    @Override
	public Selenium executeNavigation(MemoryData memory) throws Exception {

        Properties prop = memory.getRepository(Repository.WEB_NAVIGATION);
        super.setUp(prop.getProperty("SGATE_WEB_URL"));

        selenium.open(prop.getProperty("SGATE_WEB_URL")+"/login.htm");
        selenium.type("userId", prop.getProperty("username"));
        selenium.type("password", prop.getProperty("password"));
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