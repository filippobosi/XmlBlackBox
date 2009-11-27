package org.xmlblackbox.test.selenium.xmlblackbox;

import com.thoughtworks.selenium.*;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.interfaces.SeleniumNavigation;
import org.xmlblackbox.test.infrastructure.util.MemoryData;
import org.xmlblackbox.test.infrastructure.util.SeleniumEnvironment;


public class GenericLoginXBB extends SeleniumEnvironment implements SeleniumNavigation{
	private final static Logger logger = Logger.getLogger(GenericLoginXBB.class);

    @Override
	public Selenium executeNavigation(MemoryData memory) throws Exception {

        Properties prop = memory.getRepository(Repository.WEB_NAVIGATION);
        super.initialize("http://www.sgate.it", memory.getRepository(Repository.FILE_PROPERTIES));

        logger.info("selenium "+selenium);
        selenium.open(prop.getProperty("EXAMPLE_WEB_URL")+"/login.htm");
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