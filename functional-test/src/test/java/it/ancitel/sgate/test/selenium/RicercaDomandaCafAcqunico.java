/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.ancitel.sgate.test.selenium;

import com.thoughtworks.selenium.Selenium;
import it.ancitel.sgate.test.util.SgateSeleneseTestCase;
import it.ancitel.test.infrastruttura.interfaces.SeleniumNavigation;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author kewell
 */
public class RicercaDomandaCafAcqunico extends SgateSeleneseTestCase implements SeleniumNavigation{
	private final static Logger logger = Logger.getLogger(CreaDomandaEconomica.class);

    @Override
	public Selenium executeNavigation(Properties prop) throws Exception {
    	logger.debug("SELENIUM PROPERTIES : " + prop);

        selenium.open("/sgate-web/security-area/home.htm");
		selenium.click("link=Ricerca ed Estrai");
		selenium.waitForPageToLoad("30000");
		selenium.select("listaTipiId", prop.getProperty("idType"));
		selenium.type("id", prop.getProperty("parametro"));
		selenium.type("codiceFiscale", prop.getProperty("codiceFiscale"));
		selenium.click("_finish");
		selenium.waitForPageToLoad("30000");
        Thread.sleep(10000);
        assertTrue(selenium.isTextPresent(prop.getProperty("stato")));

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
