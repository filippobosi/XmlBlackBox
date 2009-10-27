package it.ancitel.sgate.test.selenium;

import com.thoughtworks.selenium.*;
import it.ancitel.sgate.test.util.SgateSeleneseTestCase;
import it.ancitel.test.infrastruttura.interfaces.SeleniumNavigation;
import java.util.Properties;
import org.apache.log4j.Logger;


public class RicercaEstraiDomande extends SgateSeleneseTestCase implements SeleniumNavigation{
	private final static Logger logger = Logger.getLogger(RicercaEstraiDomande.class);

    @Override
	public Selenium executeNavigation(Properties prop) throws Exception {

        super.setUp(prop.getProperty("SGATE_WEB_URL"));

		selenium.click("link=Ricerca ed Estrai");
		selenium.waitForPageToLoad("30000");
		selenium.click("exportFilter1");
		selenium.select("orderCriteria", "value=idDomanda");
		selenium.select("orderDirection", "value=descending");
		selenium.click("//input[@value='Ricerca']");
		selenium.waitForPageToLoad("120000");
		//selenium.click("//input[@value='Ultima']");
		        
		
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