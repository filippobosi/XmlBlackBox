/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.ancitel.sgate.test.selenium.util;

import com.thoughtworks.selenium.Selenium;
import it.ancitel.sgate.test.util.SgateSeleneseTestCase;
import it.ancitel.test.infrastruttura.interfaces.SeleniumNavigation;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author kewell
 */
public class SvuotaElencoEsitiAnagrafici extends SgateSeleneseTestCase implements SeleniumNavigation {

    private final static Logger logger = Logger.getLogger(SvuotaElencoEsitiAnagrafici.class);

    @Override
    public Selenium executeNavigation(Properties prop) throws Exception {
        logger.info("url web: " + prop.getProperty("SGATE_WEB_URL"));
        super.setUp(prop.getProperty("SGATE_WEB_URL"));

        selenium.open("/sgate-web/security-area/home.htm");
        
        while (true) {
            selenium.click("link=Visualizzazione esiti");
            selenium.waitForPageToLoad("30000");
            if (selenium.isTextPresent("Nessun risultato trovato per la ricerca effettuata")) {
                break;
            }
            selenium.click("no-all");
            selenium.waitForPageToLoad("30000");
            selenium.click("conferma");
            selenium.waitForPageToLoad("30000");
            selenium.click("salva");
            selenium.waitForPageToLoad("30000");
        }

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
