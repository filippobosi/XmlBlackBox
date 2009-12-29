package org.xmlblackbox.test.selenium.xmlblackbox;

import com.thoughtworks.selenium.*;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.interfaces.SeleniumNavigation;
import org.xmlblackbox.test.infrastructure.util.MemoryData;
import org.xmlblackbox.test.infrastructure.util.SeleniumEnvironment;


public class RicercaGoogleXBB extends SeleniumEnvironment implements SeleniumNavigation{
	private final static Logger logger = Logger.getLogger(RicercaGoogleXBB.class);

    @Override
	public Selenium executeNavigation(MemoryData memory) throws Exception {

        Properties prop = memory.getRepository(Repository.WEB_NAVIGATION);
        String webUrl = memory.getRepository(Repository.PARAMETERS).getProperty("GOOGLE_WEB_URL");
        logger.info("webUrl "+webUrl);
        webUrl = "http://www.google.it/";

        super.initialize(webUrl, memory.getRepository(Repository.FILE_PROPERTIES));

        selenium.setSpeed("0");
        selenium.open(webUrl);
		selenium.open("/");
		selenium.click("link=Ricerca avanzata");
		selenium.waitForPageToLoad("30000");
		selenium.type("as_epq", "shrek movies");
		selenium.select("lr", "label=Inglese");
		selenium.select("as_filetype", "label=Adobe Acrobat PDF (.pdf)");
		selenium.select("as_filetype", "label=qualsiasi formato");
		selenium.select("as_qdr", "label=nell'ultimo mese");
		selenium.select("num", "label=30 risultati");
		selenium.click("btnG");
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