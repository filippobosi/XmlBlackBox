package org.xmlblackbox.test.selenium.xmlblackbox;

import com.thoughtworks.selenium.*;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.interfaces.SeleniumNavigation;
import org.xmlblackbox.test.infrastructure.util.MemoryData;
import org.xmlblackbox.test.infrastructure.util.SeleniumEnvironment;


public class WebApplicationTest extends SeleniumEnvironment implements SeleniumNavigation{
	private final static Logger logger = Logger.getLogger(WebApplicationTest.class);

    @Override
	public Selenium executeNavigation(MemoryData memory) throws Exception {

        Properties prop = memory.getRepository(Repository.PARAMETERS);
        String webUrl = prop.getProperty("EXAMPLE_WEB_URL");
        super.initialize(webUrl, memory.getRepository(Repository.FILE_PROPERTIES));

        selenium.open(webUrl);
        selenium.waitForPageToLoad("30000");

        assertTrue("\"Hello World\" not found!", selenium.isTextPresent("Hello World"));
        
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