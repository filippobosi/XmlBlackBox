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
public class PresentaDomandaAltroComune extends SgateSeleneseTestCase implements SeleniumNavigation {

    private final static Logger logger = Logger.getLogger(PresentaDomandaAltroComune.class);

    @Override
    public Selenium executeNavigation(Properties prop) throws Exception {
        logger.debug("SELENIUM PROPERTIES : " + prop);

        selenium.open("/sgate-web/security-area/home.htm");
		selenium.click("link=Ricerca ed Estrai");
		selenium.waitForPageToLoad("30000");
		selenium.type("idDomanda", prop.getProperty("idDomanda"));
		selenium.click("//input[@value='Ricerca']");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Visualizza Dati");
		selenium.waitForPageToLoad("30000");
		selenium.click("_finish_presenta");
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
